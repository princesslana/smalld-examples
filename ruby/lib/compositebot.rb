require_relative 'bot'
require_relative 'pingbot'
require_relative 'catbot'

composite_bot = Bot.new do
  with $ping_bot, $cat_bot
end

composite_bot.run token: ENV['SMALLD_TOKEN'] if __FILE__ == $0
