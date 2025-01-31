import React from 'react';
import { Button } from '@mui/material';

const GenerarReporteButton = () => {
  const handleGenerarReporte = async () => {
    try {
      const response = await fetch('http://localhost:5000/api/reporte', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const blob = await response.blob();
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'reporte.xlsx';
        link.click();
      } else {
        console.error('Error al generar el reporte');
      }
    } catch (error) {
      console.error('Error de red:', error);
    }
  };

  return (
    <Button variant="contained" color="primary" onClick={handleGenerarReporte}>
      Generar Reporte xlsx
    </Button>
  );
};

export default GenerarReporteButton;
