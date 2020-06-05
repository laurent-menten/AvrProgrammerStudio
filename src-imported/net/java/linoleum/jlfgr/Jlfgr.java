package net.java.linoleum.jlfgr;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * <p>
 * Helper class for the "Java Look and Feel Graphis Repository 1.0".
 * 
 * <p>
 * See LICENSE file for graphics copyright and licensing rights.
 * 
 * @author Laurent Menten <laurent.menten@gmail.com)
 */
public enum Jlfgr
{
	// ------------------------------------------------------------------------
	// - DEVELOPMENT  ---------------------------------------------------------
	// ------------------------------------------------------------------------

	DEV_APPLET( Jlfgr.PREFIX_DEV, "Applet" ),
	DEV_APPLICATION( Jlfgr.PREFIX_DEV, "Application" ),
	DEV_APPLICATION_DEPLOY( Jlfgr.PREFIX_DEV, "ApplicationDeploy" ),
	DEV_BEAN( Jlfgr.PREFIX_DEV, "Bean" ),
	DEV_BEAN_ADD( Jlfgr.PREFIX_DEV, "BeanAdd" ),
	DEV_ENTREPRISE_JAVA_BEAN( Jlfgr.PREFIX_DEV, "EnterpriseJavaBean" ),
	DEV_ENTREPRISE_JAVA_BEAN_JAR( Jlfgr.PREFIX_DEV, "EnterpriseJavaBeanJar" ),
	DEV_HOST( Jlfgr.PREFIX_DEV, "Host" ),
	DEV_J2EE_APPLICATION( Jlfgr.PREFIX_DEV, "J2EEApplication" ),
	DEV_J2EE_APPLICATION_CLIENT( Jlfgr.PREFIX_DEV, "J2EEApplicationClient" ),
	DEV_J2EE_APPLICATION_CLIENT_ADD( Jlfgr.PREFIX_DEV, "J2EEApplicationClientAdd" ),
	DEV_J2EE_SERVER( Jlfgr.PREFIX_DEV, "J2EEServer" ),
	DEV_JAR( Jlfgr.PREFIX_DEV, "Jar" ),
	DEV_JAR_ADD( Jlfgr.PREFIX_DEV, "JarAdd" ),
	DEV_SERVER( Jlfgr.PREFIX_DEV, "Server" ),
	DEV_WAR( Jlfgr.PREFIX_DEV, "War" ),
	DEV_WAR_ADD( Jlfgr.PREFIX_DEV, "WarAdd" ),
	DEV_WEB_COMPONENT( Jlfgr.PREFIX_DEV, "WebComponent" ),
	DEV_WEB_COMPONENT_ADD( Jlfgr.PREFIX_DEV, "WebComponentAdd" ),

	// ------------------------------------------------------------------------
	// - GENERAL --------------------------------------------------------------
	// ------------------------------------------------------------------------

	GENERAL_ABOUT( Jlfgr.PREFIX_GENERAL, "About" ),
	GENERAL_ADD( Jlfgr.PREFIX_GENERAL, "Add" ),
	GENERAL_ALIGN_BOTTOM( Jlfgr.PREFIX_GENERAL, "AlignBottom" ),
	GENERAL_ALIGN_CENTER( Jlfgr.PREFIX_GENERAL, "AlignCenter" ),
	GENERAL_ALIGN_JUSTIFY_HORIZONTAL( Jlfgr.PREFIX_GENERAL, "AlignJustifyHorizontal" ),
	GENERAL_ALIGN_JUSTIFY_VERTICAL( Jlfgr.PREFIX_GENERAL, "AlignJustifyVertical" ),
	GENERAL_ALIGN_LEFT( Jlfgr.PREFIX_GENERAL, "AlignLeft" ),
	GENERAL_ALIGN_RIGHT( Jlfgr.PREFIX_GENERAL, "AlignRight" ),
	GENERAL_ALIGN_TOP( Jlfgr.PREFIX_GENERAL, "AlignTop" ),
	GENERAL_BOOKMARKS( Jlfgr.PREFIX_GENERAL, "Bookmarks" ),
	GENERAL_COMPOSE_MAIL( Jlfgr.PREFIX_GENERAL, "ComposeMail" ),
	GENERAL_CONTEXTUAL_HELP( Jlfgr.PREFIX_GENERAL, "ContextualHelp" ),
	GENERAL_COPY( Jlfgr.PREFIX_GENERAL, "Copy" ),
	GENERAL_CUT( Jlfgr.PREFIX_GENERAL, "Cut" ),
	GENERAL_DELETE( Jlfgr.PREFIX_GENERAL, "Delete" ),
	GENERAL_EDIT( Jlfgr.PREFIX_GENERAL, "Edit" ),
	GENERAL_EXPORT( Jlfgr.PREFIX_GENERAL, "Export" ),
	GENERAL_FIND( Jlfgr.PREFIX_GENERAL, "Find" ),
	GENERAL_FIND_AGAIN( Jlfgr.PREFIX_GENERAL, "FindAgain" ),
	GENERAL_HELP( Jlfgr.PREFIX_GENERAL, "Help" ),
	GENERAL_HISTORY( Jlfgr.PREFIX_GENERAL, "History" ),
	GENERAL_IMPORT( Jlfgr.PREFIX_GENERAL, "Import" ),
	GENERAL_INFORMATION( Jlfgr.PREFIX_GENERAL, "Information" ),
	GENERAL_NEW( Jlfgr.PREFIX_GENERAL, "New" ),
	GENERAL_OPEN( Jlfgr.PREFIX_GENERAL, "Open" ),
	GENERAL_PAGE_SETUP( Jlfgr.PREFIX_GENERAL, "PageSetup" ),
	GENERAL_PASTE( Jlfgr.PREFIX_GENERAL, "Paste" ),
	GENERAL_PREFERENCES( Jlfgr.PREFIX_GENERAL, "Preferences" ),
	GENERAL_PRINT( Jlfgr.PREFIX_GENERAL, "Print" ),
	GENERAL_PRINT_PREVIEW( Jlfgr.PREFIX_GENERAL, "PrintPreview" ),
	GENERAL_PROPERTIES( Jlfgr.PREFIX_GENERAL, "Properties" ),
	GENERAL_REDO( Jlfgr.PREFIX_GENERAL, "Redo" ),
	GENERAL_REFRESH( Jlfgr.PREFIX_GENERAL, "Refresh" ),
	GENERAL_REMOVE( Jlfgr.PREFIX_GENERAL, "Remove" ),
	GENERAL_REPLACE( Jlfgr.PREFIX_GENERAL, "Replace" ),
	GENERAL_SAVE( Jlfgr.PREFIX_GENERAL, "Save" ),
	GENERAL_SAVE_ALL( Jlfgr.PREFIX_GENERAL, "SaveAll" ),
	GENERAL_SAVE_AS( Jlfgr.PREFIX_GENERAL, "SaveAs" ),
	GENERAL_SEARCH( Jlfgr.PREFIX_GENERAL, "Search" ),
	GENERAL_SEND_MAIL( Jlfgr.PREFIX_GENERAL, "SendMail" ),
	GENERAL_STOP( Jlfgr.PREFIX_GENERAL, "Stop" ),
	GENERAL_TIP_OF_THE_DAY( Jlfgr.PREFIX_GENERAL, "TipOfTheDay" ),
	GENERAL_UNDO( Jlfgr.PREFIX_GENERAL, "Undo" ),
	GENERAL_ZOOM( Jlfgr.PREFIX_GENERAL, "Zoom" ),
	GENERAL_ZOOM_IN( Jlfgr.PREFIX_GENERAL, "ZoomIn" ),
	GENERAL_ZOOM_OUT( Jlfgr.PREFIX_GENERAL, "ZoomOut" ),

	// ------------------------------------------------------------------------
	// - MEDIA ----------------------------------------------------------------
	// ------------------------------------------------------------------------

	MEDIA_FAST_FORWARD( Jlfgr.PREFIX_MEDIA, "FastForward" ),
	MEDIA_MOVIE( Jlfgr.PREFIX_MEDIA, "Movie" ),
	MEDIA_PAUSE( Jlfgr.PREFIX_MEDIA, "Pause" ),
	MEDIA_PLAY( Jlfgr.PREFIX_MEDIA, "Play" ),
	MEDIA_REWIND( Jlfgr.PREFIX_MEDIA, "Rewind" ),
	MEDIA_STEP_BACK( Jlfgr.PREFIX_MEDIA, "StepBack" ),
	MEDIA_STEP_FORWARD( Jlfgr.PREFIX_MEDIA, "StepForward" ),
	MEDIA_STOP( Jlfgr.PREFIX_MEDIA, "Stop" ),
	MEDIA_VOLUME( Jlfgr.PREFIX_MEDIA, "Volume" ),

	// ------------------------------------------------------------------------
	// - NAVIGATION -----------------------------------------------------------
	// ------------------------------------------------------------------------

	NAV_BACK( Jlfgr.PREFIX_NAV, "Back" ),
	NAV_DOWN( Jlfgr.PREFIX_NAV, "Down" ),
	NAV_FORWARD( Jlfgr.PREFIX_NAV, "Forward" ),
	NAV_HOME( Jlfgr.PREFIX_NAV, "Home" ),
	NAV_UP( Jlfgr.PREFIX_NAV, "Up" ),

	// ------------------------------------------------------------------------
	// - TABLE ----------------------------------------------------------------
	// ------------------------------------------------------------------------

	TABLE_COLUMN_DELETE( Jlfgr.PREFIX_TABLE, "ColumnDelete" ),
	TABLE_COLUMN_INSERT_AFTER( Jlfgr.PREFIX_TABLE, "ColumnInsertAfter" ),
	TABLE_COLUMN_INSERT_BEFORE( Jlfgr.PREFIX_TABLE, "ColumnInsertBefore" ),
	TABLE_ROW_DELETE( Jlfgr.PREFIX_TABLE, "RowDelete" ),
	TABLE_ROW_INSERT_AFTER( Jlfgr.PREFIX_TABLE, "RowInsertAfter" ),
	TABLE_ROW_INSERT_BEFORE( Jlfgr.PREFIX_TABLE, "RowInsertBefore" ),

	// ------------------------------------------------------------------------
	// - TEXT -----------------------------------------------------------------
	// ------------------------------------------------------------------------

	TEXT_ALIGN_CENTER( Jlfgr.PREFIX_TEXT, "AlignCenter" ),
	TEXT_ALIGN_JUSTIFY( Jlfgr.PREFIX_TEXT, "AlignJustify" ),
	TEXT_ALIGN_LEFT( Jlfgr.PREFIX_TEXT, "AlignLeft" ),
	TEXT_ALIGN_RIGHT( Jlfgr.PREFIX_TEXT, "AlignRight" ),
	TEXT_BOLD( Jlfgr.PREFIX_TEXT, "Bold" ),
	TEXT_ITALIC( Jlfgr.PREFIX_TEXT, "Italic" ),
	TEXT_NORMAL( Jlfgr.PREFIX_TEXT, "Normal" ),
	TEXT_UNDERLINE( Jlfgr.PREFIX_TEXT, "Underline" ),
	;

	// ------------------------------------------------------------------------

	private static final String PREFIX_DEV = "development";
	private static final String PREFIX_GENERAL = "general";
	private static final String PREFIX_MEDIA = "media";
	private static final String PREFIX_NAV = "navigation";
	private static final String PREFIX_TABLE = "table";
	private static final String PREFIX_TEXT = "text";

	private static final String SIZE_16 = "16";
	private static final String SIZE_24 = "24";

	private static final String EXT = ".gif";

	// ------------------------------------------------------------------------

	private final String prefix;
	private final String fileName;

	private Image image16;
	private Image image24;

	// ========================================================================
	// ===
	// ========================================================================

	private Jlfgr( String prefix, String fileName )
	{
		this.prefix = prefix;
		this.fileName = fileName;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public ImageIcon getIcon16()
	{
		return new ImageIcon( getImage16() );
	}

	public ImageIcon getIcon24()
	{
		return new ImageIcon( getImage24() );
	}

	// ------------------------------------------------------------------------

	public Image getImage16()
	{
		if( image16 != null )
		{
			return image16;
		}

		return getImage( 16 );
	}

	public Image getImage24()
	{
		if( image24 != null )
		{
			return image24;
		}

		return getImage( 24 );
	}

	// ------------------------------------------------------------------------

	private Image getImage( int size )
	{
		String path = prefix + "/" + fileName;

		if( size == 16 )
		{
			path += SIZE_16;
		}
		else if( size == 24 )
		{
			path += SIZE_24;
		}
		
		path += EXT;

		// --------------------------------------------------------------------

		BufferedImage buff = null;

		try
		{
			InputStream ins = Jlfgr.class.getResourceAsStream( path );
			buff = ImageIO.read( ins );
		}
		catch( Exception e )
		{
			log.log( Level.WARNING, "Failed to load image " + this, e );

			return null;
	    }

		// --------------------------------------------------------------------

		if( size == 16 )
		{
			image16 = buff;
		}
		else if( size == 24 )
		{
			image24 = buff;
		}
		
		return buff;
	}

	// ========================================================================
	// === LOGGING ============================================================
	// ========================================================================

	private static final Logger log
		= Logger.getLogger( Jlfgr.class.getName() );
}
