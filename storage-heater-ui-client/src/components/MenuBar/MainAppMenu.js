import React, { Component } from 'react';
import { browserHistory } from 'react-router';
import Menu, {MenuItem} from 'rc-menu';
import 'rc-menu/assets/index.css';

class MainAppMenu extends Component {
     render () {
        return (
			<div>
				<Menu onSelect={this.onSelection} mode="vertical">
				  <MenuItem key="home">Home</MenuItem>
				  <MenuItem key="storage">Storage List</MenuItem>
				  <MenuItem key="view">Create New</MenuItem>			 
				</Menu>
			</div>
        );
     }

     onSelection(selection) {
        var id = selection.key;
        if (id === 'home') {
            browserHistory.push('/');
        } else if (id === 'storage') {
            browserHistory.push('/storage');
        } else if (id === 'view') {
            browserHistory.push('/view');
        }
     }
}

export default MainAppMenu;
