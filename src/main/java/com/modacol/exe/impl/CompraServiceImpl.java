package com.modacol.exe.impl;

import com.modacol.exe.dto.CompraDTO;
import com.modacol.exe.dto.DetalleCompraDTO;
import com.modacol.exe.entity.Compra;
import com.modacol.exe.entity.DetalleCompra;
import com.modacol.exe.entity.Producto;
import com.modacol.exe.entity.Proveedor;
import com.modacol.exe.entity.Usuario;
import com.modacol.exe.repository.CompraRepository;
import com.modacol.exe.repository.ProductoRepository;
import com.modacol.exe.repository.ProveedorRepository;
import com.modacol.exe.repository.UsuarioRepository;
import com.modacol.exe.service.CompraService;
import com.modacol.exe.service.FlujoCajaService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final FlujoCajaService flujoCajaService;
    private final ModelMapper modelMapper;

    public CompraServiceImpl(CompraRepository compraRepository,
                             ProveedorRepository proveedorRepository,
                             UsuarioRepository usuarioRepository,
                             ProductoRepository productoRepository,
                             ModelMapper modelMapper,
                             FlujoCajaService flujoCajaService) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.modelMapper = modelMapper;
        this.flujoCajaService = flujoCajaService;
    }

    // ==========================================================
    //                   CREAR COMPRA
    // ==========================================================
    @Override
    @Transactional
    public CompraDTO crear(CompraDTO dto) {

        // valores por defecto
        if (dto.getEstado() == null || dto.getEstado().isBlank()) dto.setEstado("BORRADOR");
        if (dto.getTipoDocumento() == null || dto.getTipoDocumento().isBlank()) dto.setTipoDocumento("ORDEN_COMPRA");
        if (dto.getFormaPago() == null || dto.getFormaPago().isBlank()) dto.setFormaPago("CONTADO");

        // generar número
        dto.setNumeroCompra(generarNumeroCompra());

        // DTO -> ENTIDAD
        Compra compra = convertToEntity(dto);

        // guardar compra + detalles por cascada
        compra = compraRepository.save(compra);

        if ("APROBADA".equalsIgnoreCase(compra.getEstado())
                || "RECIBIDA".equalsIgnoreCase(compra.getEstado())
                || "RECIBIDA_TOTAL".equalsIgnoreCase(compra.getEstado())) {

            aplicarIngresoInventario(compra);
            flujoCajaService.registrarEgresoPorCompra(compra);
        }

        return convertToDto(compra);
    }

    // ==========================================================
    //                   LISTAR / OBTENER
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public List<CompraDTO> listar() {
        return compraRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompraDTO obtenerPorId(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada: " + id));
        return convertToDto(compra);
    }

    // ==========================================================
    //                     ACTUALIZAR COMPRA
    // ==========================================================
    @Override
    @Transactional
    public CompraDTO actualizar(Long id, CompraDTO dto) {

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada: " + id));

        applyDtoToEntity(dto, compra);

        // limpiar detalles viejos
        compra.getDetalles().clear();

        // agregar nuevos
        if (dto.getDetalles() != null) {
            for (DetalleCompraDTO detDto : dto.getDetalles()) {
                if (detDto.getProductoId() == null) continue;

                DetalleCompra det = new DetalleCompra();
                det.setCompra(compra);

                Producto prod = productoRepository.findById(detDto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detDto.getProductoId()));

                det.setProducto(prod);
                det.setCantidad(detDto.getCantidad());
                det.setPrecioUnitario(detDto.getPrecioUnitario());

                BigDecimal subtotal = det.getPrecioUnitario().multiply(BigDecimal.valueOf(det.getCantidad()));
                det.setSubtotal(subtotal);

                compra.getDetalles().add(det);
            }
        }

        recalcularTotal(compra);

        compra = compraRepository.save(compra);
        return convertToDto(compra);
    }

    // ==========================================================
    //                     ELIMINAR COMPRA
    // ==========================================================
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!compraRepository.existsById(id)) {
            throw new RuntimeException("Compra no encontrada: " + id);
        }
        compraRepository.deleteById(id);
    }

    // ==========================================================
    //             FORMULARIO - CREAR COMPRA VACÍA
    // ==========================================================
    @Override
    public CompraDTO crearCompraVacia() {

        CompraDTO dto = new CompraDTO();

        dto.setFechaCompra(LocalDate.now());
        dto.setEstado("BORRADOR");
        dto.setTipoDocumento("ORDEN_COMPRA");
        dto.setFormaPago("CONTADO");

        // inicializar detalles vacíos (1 fila)
        dto.setDetalles(new ArrayList<>());
        dto.getDetalles().add(new DetalleCompraDTO());

        return dto;
    }

    @Override
    public List<String> obtenerTiposDocumento() {
        return List.of("ORDEN_COMPRA", "FACTURA_COMPRA", "NOTA_CREDITO");
    }

    @Override
    public List<String> obtenerFormasPago() {
        return List.of("CONTADO", "CREDITO_30", "CREDITO_60");
    }

    @Override
    public List<String> obtenerEstadosCompra() {
        return List.of("BORRADOR", "APROBADA", "RECIBIDA_PARCIAL", "RECIBIDA_TOTAL", "CANCELADA");
    }

    // ==========================================================
    //               MAPEOS DTO <-> ENTITY
    // ==========================================================
    private Compra convertToEntity(CompraDTO dto) {
        Compra compra = new Compra();
        applyDtoToEntity(dto, compra);

        List<DetalleCompra> detalles = new ArrayList<>();

        if (dto.getDetalles() != null) {
            for (DetalleCompraDTO detDto : dto.getDetalles()) {

                if (detDto.getProductoId() == null) continue;

                DetalleCompra detalle = new DetalleCompra();
                detalle.setCompra(compra);

                Producto producto = productoRepository.findById(detDto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detDto.getProductoId()));

                detalle.setProducto(producto);
                detalle.setCantidad(detDto.getCantidad());
                detalle.setPrecioUnitario(detDto.getPrecioUnitario());

                BigDecimal subtotal = detalle.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(detalle.getCantidad()));
                detalle.setSubtotal(subtotal);

                detalles.add(detalle);
            }
        }

        compra.setDetalles(detalles);
        recalcularTotal(compra);

        return compra;
    }

    private void applyDtoToEntity(CompraDTO dto, Compra compra) {
        compra.setNumeroCompra(dto.getNumeroCompra());
        compra.setFechaCompra(dto.getFechaCompra());
        compra.setFechaEstimadaEntrega(dto.getFechaEstimadaEntrega());
        compra.setTipoDocumento(dto.getTipoDocumento());
        compra.setFormaPago(dto.getFormaPago());
        compra.setEstado(dto.getEstado());
        compra.setObservaciones(dto.getObservaciones());

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + dto.getProveedorId()));
        compra.setProveedor(proveedor);

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getUsuarioId()));
        compra.setUsuario(usuario);
    }

    private CompraDTO convertToDto(Compra compra) {
        CompraDTO dto = new CompraDTO();

        dto.setId(compra.getId());
        dto.setNumeroCompra(compra.getNumeroCompra());
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setFechaEstimadaEntrega(compra.getFechaEstimadaEntrega());
        dto.setTipoDocumento(compra.getTipoDocumento());
        dto.setFormaPago(compra.getFormaPago());
        dto.setEstado(compra.getEstado());
        dto.setObservaciones(compra.getObservaciones());
        dto.setTotal(compra.getTotal());

        if (compra.getProveedor() != null) {
            dto.setProveedorId(compra.getProveedor().getId());
            dto.setProveedorNombre(compra.getProveedor().getRazonSocial());
        }

        if (compra.getUsuario() != null) {
            dto.setUsuarioId(compra.getUsuario().getId());
            dto.setUsuarioNombre(compra.getUsuario().getNombre());
        }

        // detalles
        List<DetalleCompraDTO> detallesDto = new ArrayList<>();

        for (DetalleCompra det : compra.getDetalles()) {

            DetalleCompraDTO d = new DetalleCompraDTO();
            d.setId(det.getId());
            d.setCompraId(compra.getId());

            if (det.getProducto() != null) {
                d.setProductoId(det.getProducto().getId());
                d.setProductoNombre(det.getProducto().getNombre());
            }

            d.setCantidad(det.getCantidad());
            d.setPrecioUnitario(det.getPrecioUnitario());
            d.setSubtotal(det.getSubtotal());

            detallesDto.add(d);
        }

        dto.setDetalles(detallesDto);
        return dto;
    }

    // ==========================================================
    //                   LOGICA DE INVENTARIO
    // ==========================================================
    private void aplicarIngresoInventario(Compra compra) {

        for (DetalleCompra det : compra.getDetalles()) {

            Producto prod = productoRepository.findById(det.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + det.getProducto().getId()));

            int stockActual = prod.getCantidad() != null ? prod.getCantidad() : 0;

            prod.setCantidad(stockActual + det.getCantidad());
            productoRepository.save(prod);
        }
    }

    // ==========================================================
    //                   UTILIDADES
    // ==========================================================

    private void recalcularTotal(Compra compra) {
        BigDecimal total = compra.getDetalles().stream()
                .map(DetalleCompra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        compra.setTotal(total);
    }

    private String generarNumeroCompra() {
        long count = compraRepository.count() + 1;
        return String.format("OC-%06d", count);
    }
}
