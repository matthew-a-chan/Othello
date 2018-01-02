package cabinet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import game.Player;
import game.Range;

public class PluginManager {
	private ArrayList<URI> scanURIs;
	private ArrayList<PluginInfo> unsortedPlugs; 
	private ArrayList<PluginInfo> sortedPlugs;


	private ArrayList<PluginInfo> gamePlugs;
	private HashMap<Class<GameState>, ArrayList<PluginInfo>> games;
	private HashMap<Class<GameState>, ArrayList<PluginInfo>> AIs;
	private ArrayList<PluginInfo> generalAIs;


	public PluginManager() {

		scanURIs = new ArrayList<URI>();

		games = new HashMap<Class<GameState>, ArrayList<PluginInfo>>();
		AIs = new HashMap<Class<GameState>, ArrayList<PluginInfo>>();
		generalAIs = new ArrayList<PluginInfo>();
		gamePlugs = new ArrayList<PluginInfo>();
		unsortedPlugs = new ArrayList<PluginInfo>();
		sortedPlugs = new ArrayList<PluginInfo>();

		/*URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		URL[] urls = loader.getURLs();
		try {
			URI uri = urls[0].toURI();
			scanURIs.add(uri);
			if( !uri.getPath().endsWith("/")){
				URI parent = uri.resolve(".");
				System.err.println(uri);
				//System.err.println(parent);
				scanURIs.add(parent);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} 

		URL[] URLs = new URL[ scanURIs.size()];
		int i = 0;
		for( URI uri : scanURIs ){
			try {
				URLs[i] = uri.toURL();
				i++;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}*/
	}

	@SuppressWarnings("unchecked")
	Integer[] getMinMaxPlayers( Class<GameState> game ){
		Integer[] minmax = new Integer[2];

		Method m;
		try {
			m = game.getMethod("numPlayersAllowed", (Class<?>[]) null );
			Range<Integer> range = (Range<Integer>)m.invoke(game.newInstance(), (Object[]) null );
			minmax[0] = range.min();
			minmax[1] = range.max();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return minmax;
	}

	public ArrayList<PluginInfo> getCompatibleDisplays( Class<GameState> g ){
		return games.get(g);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Class<Player>> getCompatibleAIs( Class<GameState> g ){
		HashMap<String, Class<Player>> ais = new HashMap<String, Class<Player>>();

		ArrayList<PluginInfo> players = AIs.get(g);
		for( PluginInfo p : players ){
			ais.put( p.name(), (Class<Player>)p.getPluginClass().asSubclass(Player.class));			
		}

		// Always add the general AIs to the list
		for( PluginInfo p : generalAIs ){
			ais.put( p.name(), (Class<Player>)p.getPluginClass().asSubclass(Player.class));
		}
		return ais;
	}

	@SuppressWarnings("unchecked")
	public HashMap<Class<GameState>, String> getGameDescriptions() {
		HashMap<Class<GameState>, String> rtn = new HashMap<Class<GameState>, String>();
		for( PluginInfo pi : gamePlugs ){
			rtn.put( (Class<GameState>) pi.getPluginClass(), pi.description());
		}
		return rtn;
	}


	@SuppressWarnings("unchecked")
	public HashMap<String, Class<GameState>> getAvailableGames() {
		HashMap<String, Class<GameState>> rtn = new HashMap<String, Class<GameState>>();
		for( PluginInfo pi : gamePlugs){
			rtn.put( pi.name(), (Class<GameState>)pi.getPluginClass());
		}
		return rtn;
	}

	public void scanPlugins(){
		for( URI uri : this.scanURIs){
			scanURI( uri, "" );
		}
	}

	public void addURI( URI add ){
		if( !scanURIs.contains(add) ){
			scanURIs.add(add);
		}
	}

	private void scanURI( URI base, String binaryForm ){
		if( base == null ){
			return;
		}

		File dir = new File( base );
		if( !dir.isDirectory() ){
			return;
		}

		//System.err.println("In Directory: " + dir.getAbsolutePath() );
		for( File file : dir.listFiles() ){
			if( file.isDirectory()) {
				String period = "";
				if( !binaryForm.isEmpty() ){
					period = ".";
				}
				scanURI( file.toURI(), binaryForm+period+file.getName());

			}

			if( file.getName().endsWith(".class" ) ){
				scanClassFile( file.toURI(), binaryForm );
			}

			if( file.getName().endsWith(".jar") ){
				scanJarFile(file.toURI());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private PluginInfo addSupportedTypes( Class<?> theClass ){
		PluginInfo found = null;
		if( theClass == null ) return null;

		int mods = theClass.getModifiers();

		if( Plugin.class.isAssignableFrom(theClass) ){
			boolean sorted = true;
			Method m;
			try {
				m = theClass.getMethod("getInfo", (Class<?>[]) null);
				found = (PluginInfo) m.invoke(null, (Object[]) null);
				if( found == null ){
					return found;
				}

				if( !Modifier.isInterface(mods) && 
						!Modifier.isAbstract(mods) ) {

					sorted = false;
					found.setPluginClass( (Class<Plugin>) theClass);

					// Game Plugin!
					if( GameState.class.isAssignableFrom(theClass) ) {
						Class<GameState> gsClass = (Class<GameState>)theClass;
						
						if( !games.containsKey(gsClass)) {
							games.put( gsClass, new ArrayList<PluginInfo>() );
						}
						else {
							games.put( gsClass, games.get(gsClass));
						}
						
						if( !AIs.containsKey(gsClass)) {
							AIs.put( gsClass, new ArrayList<PluginInfo>() );
						}
						
						if( !gamePlugs.contains(found) ){
							gamePlugs.add(found);
						}
						sorted = true;
					}
					
					// GameDisplay plugin!
					if( GameDisplay.class.isAssignableFrom(theClass) ){
						List<Class<? extends GameState>> sg = found.supportedGames();
						for( Class<? extends GameState> gs : sg ){
							ArrayList<PluginInfo> displays = games.get(gs);
							if( displays != null ) {
								displays.add(found);
								sorted = true;
							}
							else {
								ArrayList<PluginInfo> stub = new ArrayList<PluginInfo>();
								stub.add(found);
								games.put((Class<GameState>)gs,stub);
							}
						}
					}
					// Player plugin!
					if( Player.class.isAssignableFrom(theClass)){
						List<Class<? extends GameState>> sg = found.supportedGames();
						if( sg == null && !generalAIs.contains(found)){
							generalAIs.add(found);
							sorted = true;
						}
						else {
							for( Class<? extends GameState> gs : sg ){
								ArrayList<PluginInfo> ais = AIs.get(gs);
								if( ais != null ){
									ais.add(found);
									sorted = true;
								}
								else {
									ArrayList<PluginInfo> stub = new ArrayList<PluginInfo>();
									stub.add(found);
									games.put((Class<GameState>)gs,stub);
								}
							}
						}
					}

					if( !sorted ){
						unsortedPlugs.add(found);
					}
					else {
						sortedPlugs.add(found);
					}
				}


			} catch (NoSuchMethodException | SecurityException e) {
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
			}
		}

		return found;
	}

	public void scanClassFile( URI filename, String className ){
		File file = new File( filename );
		try {

			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<URLClassLoader> urlClass = URLClassLoader.class;
			Method method;
			try {
				method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
				method.setAccessible(true);
				method.invoke(urlClassLoader, new Object[]{filename.toURL()});
			}
			catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}


			if( !className.isEmpty()){
				className+=".";
			}
			className += file.getName().substring(0, file.getName().length()-6);

			//System.err.println("Loading Class: " + className );
			Class<?> theClass = Class.forName(className);

			PluginInfo found = addSupportedTypes(theClass);
			if( found != null){
				found.setPath(filename.toString());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e ){
			e.printStackTrace();
		}


	}

	public void scanJarFile( URI filename ){
		File file = new File(filename);
		//System.err.println("Jar File: " + file.getAbsolutePath());
		try{
			JarFile jar = new JarFile(file.getAbsolutePath());
			Enumeration<JarEntry> entrys = jar.entries();

			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<URLClassLoader> urlClass = URLClassLoader.class;
			Method method;
			try {
				method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
				method.setAccessible(true);
				method.invoke(urlClassLoader, new Object[]{filename.toURL()});
			}
			catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			while( entrys.hasMoreElements()){
				JarEntry je = entrys.nextElement();
				if( je.getName().endsWith(".class")){
					String className = je.getName().substring(0, je.getName().length()-6);
					className = className.replace('/', '.');
				//	System.err.println("Load class from Jar: " + className);
					try {
						Class<?> theClass = Class.forName(className);

						//	System.err.println("AddTypes: " + theClass);
						PluginInfo found = addSupportedTypes(theClass);
						if( found != null ) {
							found.setPath(filename.toString());
							//System.err.println("Found Plugin: " + filename.toString());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}		
				}
			}
			
			jar.close();

		}
		catch( MalformedURLException e ){
			e.printStackTrace();
		}
		catch(IOException e ){
			e.printStackTrace();
		}
	}

}
