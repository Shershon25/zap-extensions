import org.rm3l.datanucleus.gradle.DataNucleusApi
import org.rm3l.datanucleus.gradle.extensions.enhance.EnhanceExtension
import org.zaproxy.gradle.addon.AddOnStatus

description = "Allows you to exploit out-of-band vulnerabilities"

zapAddOn {
    addOnName.set("OAST Support")
    addOnStatus.set(AddOnStatus.BETA)

    manifest {
        author.set("ZAP Dev Team")
        url.set("https://www.zaproxy.org/docs/desktop/addons/oast-support/")

        dependencies {
            addOns {
                register("database") {
                    version.set(">= 0.1.0")
                }
                register("network") {
                    version.set(">= 0.1.0")
                }
            }
        }

        extensions {
            register("org.zaproxy.addon.oast.scripts.ExtensionOastScripts") {
                classnames {
                    allowed.set(listOf("org.zaproxy.addon.oast.scripts"))
                }
                dependencies {
                    addOns {
                        register("scripts")
                        register("graaljs")
                    }
                }
            }
        }
    }
}

crowdin {
    configuration {
        val resourcesPath = "org/zaproxy/addon/${zapAddOn.addOnId.get()}/resources/"
        tokens.put("%messagesPath%", resourcesPath)
        tokens.put("%helpPath%", resourcesPath)
    }
}

datanucleus {
    enhance(
        closureOf<EnhanceExtension> {
            api(DataNucleusApi.JDO)
            persistenceUnitName(zapAddOn.addOnId.get())
        },
    )
}

dependencies {
    compileOnly(parent!!.childProjects["database"]!!)
    compileOnly(parent!!.childProjects["graaljs"]!!)
    compileOnly(parent!!.childProjects["network"]!!)

    testImplementation(project(":testutils"))
    testImplementation(parent!!.childProjects["network"]!!)
}
