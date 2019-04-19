require_relative 'smalld-examples-ruby_jars'
java_import com.github.princesslana.smalld.Attachment
java_import com.github.princesslana.smalld.SmallD
java_import java.net.URL
java_import 'okhttp3.MediaType'

require 'json'
require 'hash_dot'
require 'ostruct'

class RAttachment
  attr_reader :name, :mime_type, :url

  def initialize(name, mime_type, url)
    @name = name
    @mime_type = mime_type
    @url = url
  end

  def to_java
    Attachment.new name, MediaType.parse(mime_type), URL.new(url)
  end
end

class Bot
  attr_accessor :smalld
  attr_reader :inits, :bots

  def initialize(&block)
    @inits = []
    @bots = []

    return unless block_given?

    if block.arity == 1
      yield self
    else
      instance_eval &block
    end
  end

  def with(*bots)
    self.bots.concat bots
  end

  def post(path, payload, *attachments)
    smalld.post path, payload.to_json, *attachments.map(&:to_java)
  end

  def method_missing(method_name, *args, &block)
    if method_name.to_s =~ /on_(.*)/
      on $1.upcase, &block
    else
      super
    end
  end

  def respond_to_missing?(method_name, include_private = false)
    method_name.to_s.start_with?('on_') || super
  end

  def on(event, &block)
    inits << lambda do |smalld|
      smalld.onGatewayPayload do |str_p|
        json_p = JSON.parse(str_p).to_dot

        block.call(json_p.d) if json_p.t == event
      end
    end
  end

  def run(opts = {})
    self.smalld = SmallD.create(opts[:token])

    inits.each { |i| i.call smalld }

    bots.each { |b| b.smalld = smalld }
    bots.map { |b| b.inits }.flatten.each { |i| i.call smalld }

    smalld.run
  rescue Exception => e
    puts e.message
    puts e.backtrace
    raise
  end
end

