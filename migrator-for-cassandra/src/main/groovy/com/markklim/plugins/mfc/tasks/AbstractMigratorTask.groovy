package com.markklim.plugins.mfc.tasks

import com.markklim.plugins.mfc.MigratorExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AbstractMigratorTask extends DefaultTask {
    public String host
    public String port
    public String user
    public String password

    public String keyspace
    public String script
    public Map placeholders

    public MigratorExtension extension

    AbstractMigratorTask() {
        super()
        setGroup('Migrator')
        extension = (MigratorExtension) getProject().getExtensions().getByName('migrator')
    }

    protected abstract void run(Map parameters)

    @TaskAction
    void runTask() {
        try {
            run(addParameters())
        } catch (Exception e) {
            handleException(e)
        }
    }

    private void handleException(Throwable throwable) {
        println "Error occurred while executing ${getName()}"
        println throwable.toString()
        throw new Throwable(throwable)
    }

    private Map addParameters() {
        Map <String,Object> parameters = [:]
        putIfSet(parameters, 'host', host, extension.host)
        putIfSet(parameters, 'port', port, extension.port)
        putIfSet(parameters, 'user', user, extension.user)
        putIfSet(parameters, 'password', password, extension.password)
        putIfSet(parameters, 'keyspace', keyspace, extension.keyspace)
        putIfSet(parameters, 'script', script, extension.script)
        putIfSet(parameters, 'placeholders', placeholders, extension.placeholders)

        parameters
    }

    private void putIfSet(Map <String,Object> parameters, String key, Object propValue, Object extensionValue) {
        if (propValue != null) {
            parameters.put(key as String, propValue)
        } else if (extensionValue != null) {
            parameters.put(key as String, extensionValue)
        }
    }
}
