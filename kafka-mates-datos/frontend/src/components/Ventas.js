import React, { useState, useEffect } from 'react';

const Ventas = () => {
  const [ventas, setVentas] = useState([]);
  const [loading, setLoading] = useState(false);

  const cargarVentas = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:5000/api/ventas');
      const data = await response.json();
      setVentas(data);
    } catch (error) {
      console.error('Error al cargar ventas:', error);
    }
    setLoading(false);
  };

  useEffect(() => {
    cargarVentas();
  }, []);

  return (
    <div>
      <h3></h3>
      {loading ? <p className="loading-message">Cargando ventas...</p> : null}
      <table className="table">
        <thead>
          <tr>
            <th>Producto</th>
            <th>IdVenta</th>
            <th>Cantidad</th>
            <th>Precio</th>
            <th>precioConImpuesto</th>
          </tr>
        </thead>
        <tbody>
          {ventas.map((venta, index) => (
            <tr key={index}>
              <td>{venta.identificador}</td>
              <td>{venta.identificadorVenta}</td>
              <td>{venta.cantidad}</td>
              <td>{venta.precioConImpuesto}</td>
              <td>{venta.precioPromocionado}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="reload-button" onClick={cargarVentas}>Recargar Ventas</button>
    </div>
  );
};

export default Ventas;
