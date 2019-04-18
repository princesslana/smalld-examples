require_relative 'smalld-examples-ruby_jars'
java_import com.github.princesslana.smalld.SmallD

require 'json'
require 'hash_dot'

class Bot
  attr_accessor :smalld
  attr_reader :inits

  def initialize
    @inits = []
  end

  def post(path, payload)
    smalld.post path, payload.to_json
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
    self.smalld = SmallD.create opts[:token]
    inits.each { |i| i.call smalld }
    smalld.run
  rescue Exception => e
    puts e.message
    puts e.backtrace
    raise
  end
end

def bot
  Bot.new.tap { |b| yield b if block_given? }
end
