package com.alumipro.mobile.data.remote;

import com.alumipro.mobile.model.Cliente;
import com.alumipro.mobile.model.LoginRequest;
import com.alumipro.mobile.model.LoginResponse;
import com.alumipro.mobile.model.NotificacionMovil;
import com.alumipro.mobile.model.Producto;
import com.alumipro.mobile.model.VentaCreateRequest;
import com.alumipro.mobile.model.VentaCreateResponse;
import com.alumipro.mobile.model.VentaDetalle;
import com.alumipro.mobile.model.VentaResumen;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/clientes")
    Call<List<Cliente>> listarClientes();

    @POST("api/clientes")
    Call<Cliente> crearCliente(@Body Cliente cliente);

    @PUT("api/clientes/{id}")
    Call<Cliente> actualizarCliente(@Path("id") int id, @Body Cliente cliente);

    @DELETE("api/clientes/{id}")
    Call<Void> eliminarCliente(@Path("id") int id);

    @GET("api/productos")
    Call<List<Producto>> listarProductos();

    @POST("api/productos")
    Call<Producto> crearProducto(@Body Producto producto);

    @PUT("api/productos/{id}")
    Call<Producto> actualizarProducto(@Path("id") int id, @Body Producto producto);

    @DELETE("api/productos/{id}")
    Call<Void> eliminarProducto(@Path("id") int id);

    @GET("api/ventas")
    Call<List<VentaResumen>> listarVentas();

    @GET("api/ventas/{id}")
    Call<VentaDetalle> obtenerVenta(@Path("id") int id);

    @POST("api/ventas")
    Call<VentaCreateResponse> crearVenta(@Body VentaCreateRequest request);

    @GET("api/movil/notificaciones")
    Call<List<NotificacionMovil>> listarNotificaciones();
}
