import React, { Component } from 'react';
import { browserHistory } from 'react-router';
import Menu, {MenuItem} from 'rc-menu';
import 'rc-menu/assets/index.css';
import { connect } from 'react-redux';
import { getUser } from '../actions/actions';

const mapStateToProps = state => ({
  user: state.user
});

const mapDispatchToProps = dispatch => ({
  onLogout: (user) => {
    dispatch(getUser(user));
  }
});

class MainAppMenu extends Component {
     render () {
        return (
			<div>
				<Menu onSelect={this.onSelection.bind(this)} mode="vertical">
				  <MenuItem key="home">Home</MenuItem>
				  <MenuItem key="storage">Storage List</MenuItem>
				  <MenuItem key="view">Create</MenuItem>
				  <MenuItem key="logout">Logout</MenuItem>
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
        } else if (id === 'logout') {
            this.logout();
        }
     }

     logout() {
        fetch('/api/login',
        {
            method: 'DELETE',
            credentials: 'same-origin'
        })
        .then(response => {
            if (response.status !== 200) {
                throw Error(response.statusText);
            } else {
                 this.props.onLogout({userName: '', authenticated: false});
                 browserHistory.push('/login');
            }
        })
        .catch((error) => {
            console.error(error)
            console.error('Failed to logout')
        });
     }
}

export default connect(mapStateToProps, mapDispatchToProps) (MainAppMenu);
