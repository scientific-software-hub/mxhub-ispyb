/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************************************************************/

package ispyb.server.common.vos;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;

import org.junit.BeforeClass;

public class EJB3Test {

	protected static Ejb3ServiceLocator serviceLocator;

	protected static Properties properties;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties ispybProperties = new Properties();
		ispybProperties.load(new FileReader(new File("build.properties")));
		String jbossPath = ispybProperties.getProperty("jboss.home") + "/server/default/conf/testJAAS.config";
		String jbossPolicy = ispybProperties.getProperty("jboss.home") + "/server/default/conf/standaloneClient.policy";

		System.out.println("Setting environment up. Fix path is required " + jbossPath);
		System.setProperty("java.security.auth.login.config", jbossPath);
		String username = "mx415";
		String password = "pimx415";
		LoginContext lc = new LoginContext("testEJB", new CallbackHandler() {
			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

			}
		});
		lc.login();
		System.setProperty("java.security.policy", jbossPolicy);
		System.setSecurityManager(new RMISecurityManager());
		properties = new Properties();
		properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		properties.put("java.naming.factory.url.pkgs", "=org.jboss.naming:org.jnp.interfaces");
		properties.put("java.naming.provider.url", "localhost:1099");
		serviceLocator = Ejb3ServiceLocator.getInstance();

	}

	protected java.sql.Date getDate(int day, int month, int year) throws ParseException {
		String date = year + "/" + month + "/" + day;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date utilDate = formatter.parse(date);
		return new java.sql.Date(utilDate.getTime());
	}

}
