require_relative 'bot'

$ping_bot = Bot.new do
  on_message_create do |msg|
    post "/channels/#{msg.channel_id}/messages", content: 'pong' if msg.content == '++ping'
  end
end

$ping_bot.run token: ENV['SMALLD_TOKEN'] if __FILE__ == $0

