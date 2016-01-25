// auto-generated by tscfg 0.1.4 on Sun Jan 24 18:20:16 PST 2016
// source: example/def.example.conf

package tscfg.example

import com.typesafe.config.Config

case class ScalaExampleCfg(
  endpoint: Endpoint
)
case class Endpoint(
  intReq: Int,
  interface: Interface,
  path: String,
  serial: Option[Int],
  url: String
)
case class Interface(
  port: Int
)
object Interface {
  def apply(c: Config): Interface = {
    Interface(
      if(c.hasPathOrNull("port")) c.getInt("port") else 8080
    )
  }
}
object Endpoint {
  def apply(c: Config): Endpoint = {
    Endpoint(
      c.getInt("intReq"),
      Interface(c.getConfig("interface")),
      c.getString("path"),
      if(c.hasPathOrNull("serial")) Some(c.getInt("serial")) else None,
      if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
    )
  }
}
object ScalaExampleCfg {
  def apply(c: Config): ScalaExampleCfg = {
    ScalaExampleCfg(
      Endpoint(c.getConfig("endpoint"))
    )
  }
}
