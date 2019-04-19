
require_relative 'bot'

require 'httparty'
require 'json'

def send_cat(smalld, channel_id)
  response = HTTParty.get  'http://aws.random.cat/meow'

  cat_url = JSON.parse(response.body)['file']

  smalld.post(
    "/channels/#{channel_id}/messages",
    '',
    RAttachment.new('cat.jpg', 'image/jpeg', cat_url))
end

$cat_bot = Bot.new do |smalld|
  smalld.on_message_create do |msg|
    send_cat smalld, msg.channel_id if msg.content == '++cat'
  end
end

$cat_bot.run token: ENV['SMALLD_TOKEN'] if __FILE__ == $0
