import React from 'react';
import { AppBar, Toolbar, Typography, Container, Grid, Paper, Box } from '@mui/material';
import Sidebar from './Sidebar';    
import Productos from './Productos';  
import Ventas from './Ventas'; 
import GenerarReporteButton from './GenerarReporteButton';

const Dashboard = () => {
  return (
    <div>
      <AppBar position="fixed">
        <Toolbar>
          <Typography variant="h6">Mi Dashboard</Typography>
        </Toolbar>
      </AppBar>
      <Grid container spacing={2} sx={{ marginTop: '64px' }}>
        <Grid item xs={2}>
          <Sidebar />
        </Grid>
        <Grid item xs={10}>
          <Container>
            <Box mb={2}>
              <Typography variant="h5">Productos</Typography>
              <Productos />
            </Box>
            <Box mb={2}>
              <Typography variant="h5">Ventas</Typography>
              <Ventas />
            </Box>
            <Box mb={2} textAlign="center">
              <GenerarReporteButton />
            </Box>
          </Container>
        </Grid>
      </Grid>
    </div>
  );
}

export default Dashboard;
