package br.com.tagme.gwt.core.client;

import org.gwtbootstrap3.client.ui.gwt.Widget;

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
import br.com.tagme.gwt.core.client.places.NewPersonPlace;
import br.com.tagme.gwt.core.client.places.IndexPlace;
import br.com.tagme.gwt.core.client.places.LoginPlace;
import br.com.tagme.gwt.core.client.places.SearchPlace;
import br.com.tagme.gwt.core.client.widgets.BottomNavbar;
import br.com.tagme.gwt.core.client.widgets.DefaultInnerPage;
import br.com.tagme.gwt.core.client.widgets.Footer;
import br.com.tagme.gwt.core.client.widgets.Menu;
import br.com.tagme.gwt.core.client.widgets.TopNavbar;
import br.com.tagme.gwt.core.client.widgets.UIContentWrapper;
import br.com.tagme.gwt.theme.tagme.client.CommonStyles;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Element;

public class CoreEntryPoint extends MVPEntryPoint{

	private static boolean firstHistoryHandleDispatched = false;
	private static TopNavbar topNavbar;
	private static BottomNavbar bottomNavbar;
	
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
		
		setLoginPlacePrefix("login");
		AppSecurity.setCustomSecurity(new AppAuth());
		setSecurity(AppAuth.getInstance());
		
		initMVP();
		
		topNavbar = new TopNavbar();
		final UIContentWrapper contentWrapper = new UIContentWrapper();
		
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
						RootPanel.get().add(new Footer());
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
	
	public static TopNavbar getTopNavbar(){
		return topNavbar;
	}
	
	public static void addBottomNavbar(Widget innerWidget){
		bottomNavbar = new BottomNavbar(innerWidget);
		RootPanel.get().add(bottomNavbar);
	}
	
	public static void removeBottomNavbar(){
		if(bottomNavbar != null){
			RootPanel.get().remove(bottomNavbar);
		}
	}
}
