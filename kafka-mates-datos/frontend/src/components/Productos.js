import React, { useState, useEffect } from 'react';

const Productos = () => {
  const [productos, setProductos] = useState([]);
  const [loading, setLoading] = useState(false);

  const cargarProductos = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:5000/api/productos');
      const data = await response.json();
      setProductos(data);
    } catch (error) {
      console.error('Error al cargar productos:', error);
    }
    setLoading(false);
  };

  useEffect(() => {
    cargarProductos();
  }, []);

  return (
    <div>
      <h3></h3>
      {loading ? <p className="loading-message">Cargando productos...</p> : null}
      <table className="table">
        <thead>
          <tr>
            <th>Categoría</th>
            <th>Producto</th>
            <th>Precio con Impuesto</th>
            <th>Precio Promocionado</th>
          </tr>
        </thead>
        <tbody>
          {productos.map((producto, index) => (
            <tr key={index}>
              <td>{producto.categoria}</td>
              <td>{producto.producto}</td>
              <td>{producto.precioConImpuesto}</td>
              <td>{producto.precioPromocionado}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="reload-button" onClick={cargarProductos}>Recargar Productos</button>
    </div>
  );
};

export default Productos;
