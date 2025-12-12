package com.modacol.exe.impl;

import com.modacol.exe.dto.DetalleVentaDTO;
import com.modacol.exe.dto.VentaDTO;
import com.modacol.exe.entity.Cliente;
import com.modacol.exe.entity.DetalleVenta;
import com.modacol.exe.entity.Producto;
import com.modacol.exe.entity.Usuario;
import com.modacol.exe.entity.Venta;
import com.modacol.exe.repository.ClienteRepository;
import com.modacol.exe.repository.ProductoRepository;
import com.modacol.exe.repository.UsuarioRepository;
import com.modacol.exe.repository.VentaRepository;
import com.modacol.exe.service.FlujoCajaService;
import com.modacol.exe.service.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final FlujoCajaService flujoCajaService;

    public VentaServiceImpl(VentaRepository ventaRepository,
                            ProductoRepository productoRepository,
                            UsuarioRepository usuarioRepository,
                            ClienteRepository clienteRepository,
                            FlujoCajaService flujoCajaService) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.flujoCajaService = flujoCajaService;
    }

    // ==================== CREAR ====================
    @Override
    @Transactional
    public VentaDTO crear(VentaDTO dto) {

        // Defaults
        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            dto.setEstado("CONFIRMADA"); // por defecto confirmada para que descuente stock
        }
        if (dto.getTipoDocumento() == null || dto.getTipoDocumento().isBlank()) {
            dto.setTipoDocumento("FACTURA_VENTA");
        }
        if (dto.getFormaPago() == null || dto.getFormaPago().isBlank()) {
            dto.setFormaPago("CONTADO");
        }
        if (dto.getFechaVenta() == null) {
            dto.setFechaVenta(LocalDate.now());
        }

        dto.setNumeroVenta(generarNumeroVenta());

        // Mapear DTO -> Entidad
        Venta venta = new Venta();
        applyDtoToEntity(dto, venta);

        // Mapear detalles DTO -> entidad
        List<DetalleVenta> detalles = new ArrayList<>();
        if (dto.getDetalles() != null) {
            for (DetalleVentaDTO detDto : dto.getDetalles()) {
                if (detDto.getProductoId() == null) continue;

                DetalleVenta det = new DetalleVenta();
                det.setVenta(venta);

                Producto prod = productoRepository.findById(detDto.getProductoId())
                        .orElseThrow(() ->
                                new RuntimeException("Producto no encontrado: " + detDto.getProductoId()));

                det.setProducto(prod);

                Integer cantidad = detDto.getCantidad() != null ? detDto.getCantidad() : 0;
                det.setCantidad(cantidad);

                BigDecimal precio = detDto.getPrecioUnitario() != null
                        ? detDto.getPrecioUnitario()
                        : (prod.getPrecioUnitario() != null ? prod.getPrecioUnitario() : BigDecimal.ZERO);
                det.setPrecioUnitario(precio);

                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
                det.setSubtotal(subtotal);

                detalles.add(det);
            }
        }
        venta.setDetalles(detalles);

        // Calcular total y validar stock
        recalcularTotal(venta);
        validarStockDisponible(venta);

        // Guardar
        venta = ventaRepository.save(venta);

        // Descontar inventario si la venta está confirmada/pagada
        if ("CONFIRMADA".equalsIgnoreCase(venta.getEstado())
                || "PAGADA".equalsIgnoreCase(venta.getEstado())) {
            aplicarSalidaInventario(venta);
            flujoCajaService.registrarIngresoPorVenta(venta);
        }

        return convertToDto(venta);
    }

    // ==================== LISTAR / OBTENER ====================

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> listar() {
        return ventaRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO obtenerPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + id));
        return convertToDto(venta);
    }

    // ==================== ACTUALIZAR ====================

    @Override
    @Transactional
    public VentaDTO actualizar(Long id, VentaDTO dto) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + id));

        // Actualizar cabecera
        applyDtoToEntity(dto, venta);

        // Limpiar detalles anteriores
        venta.getDetalles().clear();

        // Volver a cargar detalles desde el DTO
        if (dto.getDetalles() != null) {
            for (DetalleVentaDTO detDto : dto.getDetalles()) {
                if (detDto.getProductoId() == null) continue;

                DetalleVenta det = new DetalleVenta();
                det.setVenta(venta);

                Producto prod = productoRepository.findById(detDto.getProductoId())
                        .orElseThrow(() ->
                                new RuntimeException("Producto no encontrado: " + detDto.getProductoId()));

                det.setProducto(prod);
                det.setCantidad(detDto.getCantidad() != null ? detDto.getCantidad() : 0);
                det.setPrecioUnitario(detDto.getPrecioUnitario() != null ? detDto.getPrecioUnitario() : BigDecimal.ZERO);
                det.setSubtotal(det.getPrecioUnitario().multiply(BigDecimal.valueOf(det.getCantidad())));

                venta.getDetalles().add(det);
            }
        }

        recalcularTotal(venta);
        venta = ventaRepository.save(venta);

        return convertToDto(venta);
    }

    // ==================== ELIMINAR ====================

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada: " + id);
        }
        // Aquí podrías devolver stock si quieres ser más pro.
        ventaRepository.deleteById(id);
    }

    // ==================== FILTRAR ====================

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> filtrar(LocalDate desde, LocalDate hasta, Long usuarioId) {
        return ventaRepository.findAll().stream()
                .filter(v -> {
                    LocalDate fecha = v.getFechaVenta();
                    boolean okFecha = fecha != null
                            && (fecha.isEqual(desde) || fecha.isAfter(desde))
                            && (fecha.isEqual(hasta) || fecha.isBefore(hasta));

                    boolean okUsuario = (usuarioId == null)
                            || (v.getUsuario() != null
                            && v.getUsuario().getId().equals(usuarioId));

                    return okFecha && okUsuario;
                })
                .map(this::convertToDto)
                .toList();
    }

    // ==================== FORM / COMBOS ====================

    @Override
    public VentaDTO crearVentaVacia() {
        VentaDTO dto = new VentaDTO();
        dto.setFechaVenta(LocalDate.now());
        dto.setEstado("CONFIRMADA");
        dto.setTipoDocumento("FACTURA_VENTA");
        dto.setFormaPago("CONTADO");

        dto.setDetalles(new ArrayList<>());
        dto.getDetalles().add(new DetalleVentaDTO());

        return dto;
    }

    @Override
    public List<String> obtenerTiposDocumento() {
        return List.of("FACTURA_VENTA", "BOLETA", "RECIBO");
    }

    @Override
    public List<String> obtenerFormasPago() {
        return List.of("CONTADO", "CREDITO_30", "CREDITO_60");
    }

    @Override
    public List<String> obtenerEstadosVenta() {
        return List.of("BORRADOR", "CONFIRMADA", "PAGADA", "ANULADA");
    }

    // ==================== MAPEOS DTO <-> ENTIDAD ====================

    private void applyDtoToEntity(VentaDTO dto, Venta venta) {
        venta.setNumeroVenta(dto.getNumeroVenta());
        venta.setFechaVenta(dto.getFechaVenta());
        venta.setTipoDocumento(dto.getTipoDocumento());
        venta.setFormaPago(dto.getFormaPago());
        venta.setEstado(dto.getEstado());
        venta.setObservaciones(dto.getObservaciones());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException("Usuario no encontrado: " + dto.getUsuarioId()));
            venta.setUsuario(usuario);
        }

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() ->
                            new RuntimeException("Cliente no encontrado: " + dto.getClienteId()));
            venta.setCliente(cliente);
        }
    }

    private VentaDTO convertToDto(Venta venta) {
        VentaDTO dto = new VentaDTO();

        dto.setId(venta.getId());
        dto.setNumeroVenta(venta.getNumeroVenta());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setTipoDocumento(venta.getTipoDocumento());
        dto.setFormaPago(venta.getFormaPago());
        dto.setEstado(venta.getEstado());
        dto.setObservaciones(venta.getObservaciones());
        dto.setTotal(venta.getTotal());

        if (venta.getUsuario() != null) {
            dto.setUsuarioId(venta.getUsuario().getId());
            dto.setUsuarioNombre(venta.getUsuario().getNombre());
        }

        if (venta.getCliente() != null) {
            dto.setClienteId(venta.getCliente().getId());
            dto.setClienteNombre(venta.getCliente().getNombre());
        }

        List<DetalleVentaDTO> detallesDto = new ArrayList<>();
        if (venta.getDetalles() != null) {
            for (DetalleVenta det : venta.getDetalles()) {
                DetalleVentaDTO d = new DetalleVentaDTO();
                d.setId(det.getId());
                d.setVentaId(venta.getId());

                if (det.getProducto() != null) {
                    d.setProductoId(det.getProducto().getId());
                    d.setProductoNombre(det.getProducto().getNombre());
                }

                d.setCantidad(det.getCantidad());
                d.setPrecioUnitario(det.getPrecioUnitario());
                d.setSubtotal(det.getSubtotal());

                detallesDto.add(d);
            }
        }
        dto.setDetalles(detallesDto);

        return dto;
    }

    // ==================== INVENTARIO / STOCK ====================

    private void validarStockDisponible(Venta venta) {
        if (venta.getDetalles() == null) return;

        for (DetalleVenta det : venta.getDetalles()) {
            if (det.getProducto() == null) continue;

            Producto prod = productoRepository.findById(det.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + det.getProducto().getId()));

            int stockActual = prod.getCantidad() != null ? prod.getCantidad() : 0;
            int cantidadVendida = det.getCantidad() != null ? det.getCantidad() : 0;

            if (cantidadVendida > stockActual) {
                throw new RuntimeException("Stock insuficiente para el producto "
                        + prod.getNombre() + " (stock: " + stockActual
                        + ", pedido: " + cantidadVendida + ")");
            }
        }
    }

    private void aplicarSalidaInventario(Venta venta) {
        if (venta.getDetalles() == null) return;

        for (DetalleVenta det : venta.getDetalles()) {
            if (det.getProducto() == null) continue;

            Producto prod = productoRepository.findById(det.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + det.getProducto().getId()));

            int stockActual = prod.getCantidad() != null ? prod.getCantidad() : 0;
            int cantidadVendida = det.getCantidad() != null ? det.getCantidad() : 0;

            prod.setCantidad(stockActual - cantidadVendida);
            productoRepository.save(prod);
        }
    }

    // ==================== UTILIDADES ====================

    private void recalcularTotal(Venta venta) {
        BigDecimal total = BigDecimal.ZERO;
        if (venta.getDetalles() != null) {
            for (DetalleVenta det : venta.getDetalles()) {
                if (det.getSubtotal() != null) {
                    total = total.add(det.getSubtotal());
                }
            }
        }
        venta.setTotal(total);
    }

    private String generarNumeroVenta() {
        long count = ventaRepository.count() + 1;
        return String.format("V-%06d", count); // V-000001, etc.
    }
}
