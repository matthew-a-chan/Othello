package cabinet;

import java.util.List;

/**
 * Represents a description of a plugin.  Every plugin must provide a method which 
 * returns a PluginInfo to be classified as a plugin.
 * 
 * The method declaration required is:
 * public static PluginInfo getInfo();
 * @author bsea
 *
 */
public abstract class PluginInfo {
	private String path;
	private String ID;
	private Class<? extends Plugin> myClass;
	
	public PluginInfo(){
		path = "";
		ID = "";
	}
		
	/**
	 * Set the class of this object is describing
	 * @param mc the class of the plugin
	 * @return true if mc is non-null, false otherwise
	 */
	public final boolean setPluginClass(Class<? extends Plugin> mc ) {
		boolean rtn = false;
		if( mc != null ){
			myClass = mc;
			rtn = true;
		}
		return rtn;
	}
	/**
	 * Get the class object of the plugin
	 * @return the plugin's class
	 */
	public final Class<? extends Plugin> getPluginClass(){
		return myClass;
	}
	
	/**
	 * Setup a unique id for the plugin.  Currently unused.
	 * @param id the id to set
	 * @return true if id is non-null and not empty
	 */
	public final boolean setID( String id ) {
		boolean rtn = false;
		if( id != null && id.trim().length() > 0 ){
			this.ID = id;
			rtn = true;
		}
		return rtn;
	}
	/**
	 * Get the set ID of the plugin
	 * @return the id
	 */
	public final String getID(){
		return ID;
	}
	
	/**
	 * Sets the location of the file containing the plugin's class
	 * @param path the path to the class file
	 * @return true if the path if non-null and not empty
	 */
	public final boolean setPath( String path ) {
		boolean rtn = false;
		if( path != null && path.trim().length() > 0 ){
			this.path = path;
			rtn = true;
		}
		return rtn;
	}
	/**
	 * Get the absolute path to the plugin described by this object
	 * @return an absolute path
	 */
	public final String getPath() {
		return path;
	}
	
	/**
	 * Provides information regarding the plugin
	 * @return the name of the plugin
	 */
	public abstract String name();
	/**
	 * Provides information regarding the plugin
	 * @return the description of the plugin
	 */
	public abstract String description();
	/**
	 * Provides the type of plugin this object is representing.  
	 * Current types supported: GameState, GameDisplay, Player
	 * @return the type of this plugin
	 */
	public abstract Class<? extends Plugin> type();
	
	/**
	 * Provides a list of supported games.
	 * @return a list of supported games or null if supporting all games.  An empty list means this plugin supports no games
	 */
	public abstract List<Class<? extends GameState> > supportedGames();
}
