// generated by tscfg 0.3.0 on Tue Mar 01 18:17:48 PST 2016
// source: example/def.example.conf

package tscfg.example

object ScalaExampleCfg {
  object Endpoint {
    object Interface {
      def apply(c: com.typesafe.config.Config): Interface = {
        Interface(
          if(c.hasPathOrNull("port")) c.getInt("port") else 8080,
          if(c.hasPathOrNull("type")) Some(c.getString("type")) else None
        )
      }
    }
    case class Interface(
      port : Int,
      `type` : Option[String]
    ) {
      override def toString: String = toString("")
      def toString(i:String): String = {
        i+ "port = " + this.port + "\n"+
        i+ "type = " + (if(this.`type`.isDefined) "Some(" +'"' +this.`type`.get+ '"' + ")" else "None") + "\n"
      }
    }
    def apply(c: com.typesafe.config.Config): Endpoint = {
      Endpoint(
        c.getInt("intReq"),
        Interface(c.getConfig("interface")),
        c.getString("path"),
        if(c.hasPathOrNull("serial")) Some(c.getInt("serial")) else None,
        if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
      )
    }
  }
  case class Endpoint(
    intReq : Int,
    interface : ScalaExampleCfg.Endpoint.Interface,
    path : String,
    serial : Option[Int],
    url : String
  ) {
    override def toString: String = toString("")
    def toString(i:String): String = {
      i+ "intReq = " + this.intReq + "\n"+
      i+ "Interface(\n" + this.interface.toString(i+"    ") +i+ ")\n"+
      i+ "path = " + '"' + this.path + '"' + "\n"+
      i+ "serial = " + this.serial + "\n"+
      i+ "url = " + '"' + this.url + '"' + "\n"
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaExampleCfg = {
    ScalaExampleCfg(
      Endpoint(c.getConfig("endpoint"))
    )
  }
}
case class ScalaExampleCfg(
  endpoint : ScalaExampleCfg.Endpoint
) {
  override def toString: String = toString("")
  def toString(i:String): String = {
    i+ "Endpoint(\n" + this.endpoint.toString(i+"    ") +i+ ")\n"
  }
}
