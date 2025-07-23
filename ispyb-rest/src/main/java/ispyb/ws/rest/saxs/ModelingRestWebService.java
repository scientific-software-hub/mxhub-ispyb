package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.utils.reader.pdb.PDBFactoryProducer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Path("/")
public class ModelingRestWebService extends SaxsRestWebService {
	
	private final static Logger logger = Logger.getLogger(ModelingRestWebService.class);
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/modeling/pdb/get")
	@Produces({ "application/json" })
	public Response getPDBModels(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@FormParam("models") String models,
			@FormParam("superpositions") String superpositionsParam) {
		
		String methodName = "getPDBModels";
		long start = this.logInit(methodName, logger, token, proposal);
		try{
			/**
			 * [{modelId=5034, color=0xFF6600, opacity=0.5}{modelId=5036,
			 * color=0x00CC00, opacity=0.5}]
			 **/
			List<Integer> superpositions = this.parseToInteger(superpositionsParam);

			Type mapType = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
			ArrayList<HashMap<String, String>> modelList = new Gson().fromJson(models, mapType);

			HashMap<String, List<HashMap<String, Object>>> map = PDBFactoryProducer.get(modelList, superpositions);
			
			this.logFinish(methodName, start, logger);
			return this.sendResponse(map);
		}
		catch(Exception e){
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
}