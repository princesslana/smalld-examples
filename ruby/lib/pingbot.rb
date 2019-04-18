

require_relative 'bot'

ping_bot = bot do |s|
  s.on_message_create do |msg|
    s.post "/channels/#{msg.channel_id}/messages", content: 'pong' if msg.content == '++ping'
  end
end

ping_bot.run token: ENV['SMALLD_TOKEN']

