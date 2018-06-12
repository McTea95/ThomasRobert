package de.htw.ai.kbe.songsRX;

import org.glassfish.jersey.server.ResourceConfig;

import de.htw.ai.kbe.songsRX.di.DependencyBinder;

public class App extends ResourceConfig 
{
    public App() {
    	register(new DependencyBinder());
    	packages(true, "de.htw.ai.kbe.songsRX");
    }
}
