# EranoAPI-Parent
This project is a Java-based API parent module.
- Modular structure
- Easy setup
- Open source under the MIT license

## Usage

You can use the project modules in your own applications.  
For documentation and examples, please check the source code.

Note: You have to clone EranoAPI-Parent and execute "mvn clean install" from the project root.
Then you have Dist/Target/EranoAPI.jar for your servers /plugins folder. 
EranoAPI-Parent/EranoAPI project is enaugh for api usage. No need for NMS projects in your local.
You can fetch this depedency from github:

```xml
<repository>
  <id>github</id>
  <name>GitHub Erano01 Spigot Packages</name>
  <url>https://maven.pkg.github.com/Erano01/EranoAPI-Parent</url>
</repository>
```

```xml
<dependency>
  <groupId>me.erano.com</groupId>
  <artifactId>eranoapi</artifactId>
  <version>1.0</version>
</dependency>
```

## Contributing

Feel free to open a pull request or create an issue to contribute.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
