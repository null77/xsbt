import sbt._

class WebappBuild(info: ProjectInfo) extends DefaultWebProject(info) {

	override def libraryDependencies =
		if("jetty7.0".asFile.exists)
			jetty70Dependencies
		else if("jetty7.1".asFile.exists)
			jetty71Dependencies
		else if("jetty7.2".asFile.exists)
			jetty72Dependencies
		else
			jetty6Dependencies

	def jetty6Dependencies =
		Set("org.mortbay.jetty"          % "servlet-api-2.5"         % "6.1.14"  % "provided->default",
		"org.mortbay.jetty"          % "jetty"                   % "6.1.14"  % "test->default")
	def jetty70Dependencies =
		Set("javax.servlet" % "servlet-api" % "2.5" % "provided",
		"org.eclipse.jetty" % "jetty-webapp" % "7.0.1.v20091125" % "test")
	def jetty71Dependencies =
		Set("javax.servlet" % "servlet-api" % "2.5" % "provided",
		"org.eclipse.jetty" % "jetty-webapp" % "7.1.6.v20100715" % "test")
	def jetty72Dependencies =
		Set("javax.servlet" % "servlet-api" % "2.5" % "provided",
		"org.eclipse.jetty" % "jetty-webapp" % "7.2.0.v20101020" % "test")

	def indexURL = new java.net.URL("http://localhost:" + jettyPort)
	def indexFile = new java.io.File("index.html")
	override def jettyPort = 7123
	import Process._
	lazy val getPage = execTask { indexURL #> indexFile }
	lazy val checkPage = task { args => task { checkHelloWorld(args.mkString(" ")) } dependsOn getPage }
	
	private def checkHelloWorld(checkString: String) =
	{
		val value = xsbt.FileUtilities.read(indexFile)
		if(value.contains(checkString)) None else Some("index.html did not contain '" + checkString + "' :\n" +value)
	}
}
