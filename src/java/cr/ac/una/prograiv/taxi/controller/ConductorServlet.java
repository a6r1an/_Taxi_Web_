package cr.ac.una.prograiv.taxi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import cr.ac.una.prograiv.taxi.domain.*;
import cr.ac.una.prograiv.taxi.bl.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author _Adrián_Prendas_
 */
@WebServlet(name = "ConductorServlet", urlPatterns = {"/DriverServices"})
public class ConductorServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("text/xml");
            RuntimeTypeAdapterFactory<Jsonable> rta = RuntimeTypeAdapterFactory.of(Jsonable.class, "_class")
                    .registerSubtype(Usuario.class, "Usuario")
                    .registerSubtype(Direccion.class, "Direccion")
                    .registerSubtype(Conductor.class, "Conductor")
                    .registerSubtype(Vehiculo.class, "Vehiculo")
                    .registerSubtype(Viaje.class, "Viaje");

            Gson gson = new GsonBuilder().registerTypeAdapterFactory(rta).setDateFormat("dd/MM/yyyy").create();
            String json;

            ConductorBL cBL = new ConductorBL();
            BaseBL bbl = new BaseBL();
            Conductor driver;

            String accion = request.getParameter("action");
System.out.println("accion: " + accion);
            switch (accion) {
                case "getConductores":
                    List<Conductor> listaConductores = cBL.findAll(Conductor.class.getName());

                    json = gson.toJson(listaConductores);
System.out.println(json);
                    out.print(json);
                    break;
                case "getConductorId":
                    try {
                        json = gson.toJson(bbl.getDao(Conductor.class.getName()).findById(request.getParameter("id")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        json = gson.toJson(new Exception("Error en el servidor: no se encontrol el Conductor : " + request.getParameter("id")));
                    }
System.out.println(json);
                    out.print(json);
                    break;
                case "deleteConductorId":
                    try{
                        bbl.getDao(Conductor.class.getName()).delete(
                                bbl.getDao(Conductor.class.getName()).findById(
                                request.getParameter("id"))
                        );
                        json = gson.toJson(new Exception("Se elimino el Conductor con exito"));
                    }catch(Exception e){
                        e.printStackTrace();
                        json = gson.toJson(new Exception("Error en el servidor no se pudo eliminar el Conductor : " + request.getParameter("id")));
                    }
System.out.println(json);
                    out.print(json);
                    break;
                case "editConductor":
                    json = request.getParameter("driver");
                    try{
                        bbl.getDao(Vehiculo.class.getName()).merge(
                                gson.fromJson(json, Conductor.class).getVehiculo()
                        );
                        bbl.getDao(Conductor.class.getName()).merge(
                                gson.fromJson(json, Conductor.class)
                        );
                        json = gson.toJson(new Exception("Se modifico el Conductor con exito"));
                    }catch(Exception e){
                        e.printStackTrace();
                        json = gson.toJson(new Exception("Error en el servidor no se pudo eliminar el Conductor : " + request.getParameter("id")));
                    }
System.out.println(json);
                    out.print(json);
                    break;
                case "saveConductor":
                    json = request.getParameter("driver");
                    driver = gson.fromJson(json, Conductor.class);
System.out.println(driver);
                    try{
                        bbl.getDao(Conductor.class.getName()).save(driver);
System.out.println("se almaceno el Conductor correctamente");
                    json = gson.toJson(new Exception("se almaceno el Conductor correctamente"));
                    }catch(Exception e){
                        json = gson.toJson(new Exception("Error en el servidor no se pudo almacenar el Conductor"));
                        e.printStackTrace();
                    }
                    out.write(json);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
