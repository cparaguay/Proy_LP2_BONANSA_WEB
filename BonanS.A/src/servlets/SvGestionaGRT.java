package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

import com.bonansa.beans.GuiaRemisionTransportistaDTO;
import com.bonansa.beans.SolicitudOrdenRecojoDTO;
import com.bonansa.services.GuiaRemisionTransportistaService;
import com.google.gson.Gson;

/**
 * Servlet implementation class SvGestionaGRT
 */
@WebServlet("/SvGestionaGRT")
public class SvGestionaGRT extends HttpServlet {
	private static final long serialVersionUID = 1L;

	GuiaRemisionTransportistaService sGRT=new GuiaRemisionTransportistaService();
	ArrayList<GuiaRemisionTransportistaDTO> listadoDGRT=new ArrayList<GuiaRemisionTransportistaDTO>();
	Gson    obJson=new Gson();
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String operacion=request.getParameter("operacion");
		
		if (operacion.equals("registrar")) 
		{
			registrar(request, response);
		}
		else if (operacion.equals("agregarGRyFACT")) 
		{
		this.agregarGRyFACT(request, response);	
		}
	}
	
	
	
	private void registrar(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			//Capturando datos para tb_GRT
			String   idEmpleado=request.getParameter("txtIdEmpleado");
			String   idVehiculo=request.getParameter("txtIdVehiculo");
			String   fecInicioTraslado=request.getParameter("txtFechaTraslado");
			String   idCliRemitente=request.getParameter("txtIdClienteRemi");
			String   direcCliRemitente=request.getParameter("txtDireccionRemitente");
			String   nomCliDestinatario=request.getParameter("txtNomCliDestinatario");
			String   apepaCliDestinatario=request.getParameter("txtNomCliDestinatario");
			String   apemaCliDestinatario=request.getParameter("txtApeMaCliDest");
			int      idTipoDocumento=Integer.parseInt(request.getParameter("cboTipoDocumento"));
			String   numDocumentoCliDestinatario=request.getParameter("txtNumDocumentoCliDest");
			String   direcCliDestinatario=request.getParameter("txtDireccionCliDest");
			String   fechaMinTraslado=request.getParameter("txtFechaMinEntrega");
			String   fechaMaxTraslado=request.getParameter("txtFechaMaxEntrega");
			
			GuiaRemisionTransportistaDTO grt=new GuiaRemisionTransportistaDTO();
			grt.setIdEmpleado(idEmpleado);
			grt.setIdVeh(idVehiculo);
			grt.setFecInicioTraslado(fecInicioTraslado);
			grt.setIdCliRemitente(idCliRemitente);
			grt.setDirecCliRemitente(direcCliRemitente);
			grt.setNomCliDestinatario(nomCliDestinatario);
			grt.setApepaCliDestinatario(apepaCliDestinatario);
			grt.setApemaCliDestinatario(apemaCliDestinatario);
			grt.setIdTipoDocId(idTipoDocumento);
		    grt.setNumDocCliDestinatario(numDocumentoCliDestinatario);
		    grt.setDirecClienteDestinatario(direcCliDestinatario);
		    grt.setFecMinEntrega(fechaMinTraslado);
		    grt.setFecMaxEntrega(fechaMaxTraslado);
		    
			
		/**
		 * Capturamos la session actual
		 */
		 HttpSession miSesion=request.getSession();
		
		/**
		 * Capturamos el ArrayList de ListadoDGRT a nivel de session y casteamos
		 */
		ArrayList<GuiaRemisionTransportistaDTO> listadoDGRT=(ArrayList<GuiaRemisionTransportistaDTO>)miSesion.getAttribute("s_listadoGRT");

		String   ip_idEmpleadoR=(String)miSesion.getAttribute("idEmpleado");

		
		String idSOR=request.getParameter("txtIdSOR");
		
		
		int r=sGRT.registrarGRT(grt, ip_idEmpleadoR, idSOR);
			
			
		} catch (Exception e) {
			System.out.println("Error registrarGRT: "+e);
		}
		
	}



	private void agregarGRyFACT(HttpServletRequest request, HttpServletResponse response)
	{
		
		try 
		{

					/**
					 * Capturamos la session actual
					 */
					HttpSession miSesion=request.getSession();
					/**
					 * Capturamos el ArrayList de ListadoDGRT a nivel de session y casteamos
					 */
					ArrayList<GuiaRemisionTransportistaDTO> listadoDGRT=(ArrayList<GuiaRemisionTransportistaDTO>)miSesion.getAttribute("s_listadoGRT");
		
					//Capturamos los datos para actualizar una posicion del ArrayList
					String descTraslado=request.getParameter("descTraslado");
					String txtNumGr=request.getParameter("txtNumGr");
					String txtNumFact=request.getParameter("txtNumFact");

					
					//Recoremos la lista que capturamos previamente a nivel de sesion
					for (int i = 0; i < listadoDGRT.size(); i++) 
					{
						//Si es igual a la descripcin que capturamos, actualizaremos toda esa fila
						//Con los numer de gr y facturas.
						if (listadoDGRT.get(i).getDescTraslado().equals(descTraslado)) 
						{
							int    cantidad=listadoDGRT.get(i).getCantidad();
							int    idTipoUnidadMedida=listadoDGRT.get(i).getIdTipoUnidadMedida();
							String descTipoUnidadMedida=listadoDGRT.get(i).getDescTipoUnidadMedida();
							double pesoKg=listadoDGRT.get(i).getPesoKg();
							
							
							GuiaRemisionTransportistaDTO nuevolistadoDGRT=new GuiaRemisionTransportistaDTO();
							
							nuevolistadoDGRT.setDescTraslado(descTraslado);
							nuevolistadoDGRT.setCantidad(cantidad);
							nuevolistadoDGRT.setDescTipoUnidadMedida(descTipoUnidadMedida);
							nuevolistadoDGRT.setIdTipoUnidadMedida(idTipoUnidadMedida);
							nuevolistadoDGRT.setPesoKg(pesoKg);
							nuevolistadoDGRT.setNumCodGR(txtNumGr);
							nuevolistadoDGRT.setNumCodFT(txtNumFact);
							
							listadoDGRT.set(i, nuevolistadoDGRT);
							
						}
						
						
						
					}
					
					String json=obJson.toJson(listadoDGRT);
					PrintWriter out;
					
					out = response.getWriter();
					out.println(json);

			
		} 
		catch (Exception e) 
		{
		System.out.println("Error en agregarGRyFACT SvGestionaGRT: "+e);
		}
		
	}

}
