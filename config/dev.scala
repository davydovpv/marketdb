import com.ergodicity.marketdb.core.MarketDb
import com.ergodicity.marketdb.{MarketDbConfig, KestrelLoader, KestrelConfig}
import com.twitter.ostrich.admin.config._
import java.net.InetSocketAddress

new MarketDbConfig {
  admin.httpPort = 9000

  socketAddress = Some(new InetSocketAddress(10333))

  tradesTable = "test-market-trades"
  ordersTable = "test-market-orders"
  uidTable = "test-market-uid"

  val kestrelLoaderService = (marketDB: MarketDb) => {
    new KestrelLoader(marketDB, KestrelConfig(Seq("localhost:22133"), "trades", "orders", 30))
  }

  services = Seq(kestrelLoaderService)

  admin.statsNodes = new StatsConfig {
    reporters = new JsonStatsLoggerConfig {
      loggerName = "stats"
      serviceName = "marketDB"
    } :: new TimeSeriesCollectorConfig
  }
}
