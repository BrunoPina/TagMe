package br.com.tagme.gwt.core.client;

import br.com.sankhya.place.gwt.auth.client.AppAuth;
import br.com.sankhya.place.gwt.auth.client.AppAuthEvent;
import br.com.sankhya.place.gwt.auth.client.AppAuthEventHandler;
import br.com.sankhya.place.gwt.commons.client.security.AppSecurity;
import br.com.sankhya.place.gwt.commons.utils.client.XMLUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;
import br.com.sankhya.place.gwt.mvp.client.MVPEntryPoint;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.tagme.gwt.core.client.activities.CoreActivityFactory;
import br.com.tagme.gwt.core.client.places.IndexPlace;
import br.com.tagme.gwt.core.client.places.LoginPlace;
import br.com.tagme.gwt.core.client.places.NewPersonPlace;
import br.com.tagme.gwt.core.client.places.PersonDetailsPlace;
import br.com.tagme.gwt.core.client.places.SearchPlace;
import br.com.tagme.gwt.core.client.widgets.DefaultInnerPage;
import br.com.tagme.gwt.core.client.widgets.FluidUIContentWrapper;
import br.com.tagme.gwt.core.client.widgets.Footer;
import br.com.tagme.gwt.core.client.widgets.Menu;
import br.com.tagme.gwt.core.client.widgets.TopNavbar;
import br.com.tagme.gwt.theme.tagme.client.CommonStyles;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Element;

public class CoreEntryPoint extends MVPEntryPoint{

	private static boolean firstHistoryHandleDispatched = false;
	private static TopNavbar topNavbar;
	//private static BottomNavbar bottomNavbar;
	private static Footer footer;
	
	@Override
	public void onModuleLoad() {
		CommonStyles.INSTANCE.css().ensureInjected();
		
		XMLServiceProxy.setAppNavigation(new PlaceController());
		
		addApplicationActivityFactory(new CoreActivityFactory());
		
		addApplicationActivityFactory(new IndexPlace.Initializer());
		addApplicationTokenizer(IndexPlace.PREFIX, new IndexPlace.Initializer());
		
		addApplicationActivityFactory(new LoginPlace.Initializer());
		addApplicationTokenizer(LoginPlace.PREFIX, new LoginPlace.Initializer());
		
		addApplicationActivityFactory(new SearchPlace.Initializer());
		addApplicationTokenizer(SearchPlace.PREFIX, new SearchPlace.Initializer());
		
		addApplicationActivityFactory(new NewPersonPlace.Initializer());
		addApplicationTokenizer(NewPersonPlace.PREFIX, new NewPersonPlace.Initializer());
		
		addApplicationActivityFactory(new PersonDetailsPlace.Initializer());
		addApplicationTokenizer(PersonDetailsPlace.PREFIX, new PersonDetailsPlace.Initializer());
		
		//addApplicationActivityFactory(new WizardPlace.Initializer());
		//addApplicationTokenizer(WizardPlace.PREFIX, new WizardPlace.Initializer());
		
		setLoginPlacePrefix("login");
		AppSecurity.setCustomSecurity(new AppAuth());
		setSecurity(AppAuth.getInstance());
		
		initMVP();
		
		topNavbar = new TopNavbar();
		final FluidUIContentWrapper contentWrapper = new FluidUIContentWrapper();
		footer = new Footer();
		//final UIContentWrapper contentWrapper = new UIContentWrapper();
		
		setMainApplicationContainer(contentWrapper.getMainContainer());
		setApplicationOuterContainer(contentWrapper.getOuterContainer());
		
		DefaultInnerPage.setMenu(new Menu());
		
		AppAuth.getInstance().addAppAuthHandler(new AppAuthEventHandler() {
			
			@Override
			public void onAppAuthChange(AppAuthEvent event) {
				AppAuth.getInstance().removeAppAuthHandler(this);
				if(event.typeLoginVerified() || event.typeLoginNotVerified()){
					if(!firstHistoryHandleDispatched){
						firstHistoryHandleDispatched = true;
						RootPanel.get().add(topNavbar);
						RootPanel.get().add(contentWrapper);
						RootPanel.get().add(footer);
						//RootPanel.get().add(contentWrapper);
						MVPEntryPoint.handleCurrentHistory();
					}
				}
			}
		});
		
		XMLServiceProxy serviceProxy = new XMLServiceProxy(this);
		serviceProxy.call("auth@VerifyAuthzService", null, new XMLCallback() {
			
			@Override
			public void onResponseReceived(Element response) {
				Element authElem = XMLUtils.getFirstChild(response, "auth");
				
				String authString = "";
				
				if(authElem != null){
					authString = XMLUtils.getNodeValue(authElem);
				}
				
				AppAuth.getInstance().tryAuth(authString);
			}
			
			@Override
			public boolean onError(ServiceProxyException e) {
				AppAuth.getInstance().setLoginNotVerified(e.getMessage());
				return false;
			}
		});
		
	}
	
	public static void showControls(){
		if(topNavbar.getParent() == null){
			RootPanel.get().insert(topNavbar, 0);
		}
		if(footer.getParent() == null){
			RootPanel.get().add(footer);
		}
	}
	
	public static void hideControls(){
		if(topNavbar.getParent() != null){
			RootPanel.get().remove(topNavbar);
		}
		if(footer.getParent() != null){
			RootPanel.get().remove(footer);
		}
	}
	
	public static TopNavbar getTopNavbar(){
		return topNavbar;
	}
	
	/*public static void addBottomNavbar(Widget innerWidget){
		bottomNavbar = new BottomNavbar(innerWidget);
		RootPanel.get().add(bottomNavbar);
	}
	
	public static void removeBottomNavbar(){
		if(bottomNavbar != null){
			RootPanel.get().remove(bottomNavbar);
		}
	}*/
}
