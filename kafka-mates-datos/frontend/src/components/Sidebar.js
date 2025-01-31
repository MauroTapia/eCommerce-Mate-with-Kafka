import React from 'react';
import { List, ListItem, ListItemText, Drawer, Divider } from '@mui/material';

const Sidebar = () => {
  return (
    <Drawer variant="permanent" sx={{ width: 240, flexShrink: 0 }}>
      <div style={{ width: '240px' }}>
        <List>
          <ListItem>
            <ListItemText primary="Kafka Mates Hiberus" />
          </ListItem>
          <ListItem>
            <ListItemText primary="By Mauro Tapia" />
          </ListItem>
        </List>
      </div>
    </Drawer>
  );
};

export default Sidebar;
