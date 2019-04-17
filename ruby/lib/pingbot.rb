require_relative 'smalld-examples-ruby_jars'
java_import com.github.princesslana.smalld.SmallD

require 'json'
require 'hash_dot'


SmallD.run(ENV['SMALLD_TOKEN'].to_s) do |smalld|
  reply = lambda do |opts|
    channel = opts[:to].channel_id
    msg = { 'content' => opts[:with] }.to_json

    smalld.post "/channels/#{channel}/messages", msg
  end

  on_message_create = lambda do |msg|
    reply.call to: msg, with: 'pong' if msg.content == '++ping'
  end

  smalld.onGatewayPayload do |payload|
    j = JSON.parse(payload).to_dot

    on_message_create.call(j.d) if j.t == 'MESSAGE_CREATE'
  end
end

