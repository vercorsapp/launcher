package app.vercors.instance.launch

class JavaVersionException(val javaVersion: Int) : Exception("Java path needs to be set for version $javaVersion")