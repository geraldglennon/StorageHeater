import React, { Component } from 'react';
import MainAppMenu from './components/MenuBar/MainAppMenu';
import DisplayTable from './components/DisplayTable/DisplayTable';
import Details from './components/Storage/view/View';
import Welcome from './components/Welcome/Welcome';
import Login from './components/Login/Login';
import { getUser } from './components/actions/actions';

import './App.css';

import { connect } from 'react-redux';

const mapStateToProps = state => ({
  user: state.user,
});

const mapDispatchToProps = dispatch => ({
  onAuthenticate: (user) => {
    dispatch(getUser(user));
  }
});

import { Router, Route, browserHistory } from 'react-router'

class App extends Component {

    constructor () {
        super();
        this.state = {
           isAuthenticated: false,
           user: {}
        };
    }

    componentDidMount() {
        document.title = "Storage Heater"
    }

    componentWillMount() {
       this.authenticate();
    }

    render () {

        return (
            <div className="App">
                <div className="App-header">    
                    <h2>Storage Heater</h2>
                </div>
                <div className="app-body">
                    {this.props.user && this.props.user.authenticated &&
                        <div className="app-menu">
                            <MainAppMenu />
                        </div>
                    }
                    <div className="app-content">
                        <Router history={browserHistory}>
                            <Route path="/" component={Welcome} />
                            <Route path="/login" component={Login} />
                            <Route path="/storage" component={DisplayTable} />
                            <Route path="/view" component={Details} />
                            <Route path="*" component={Welcome} />
                        </Router>
                    </div>
                </div>
            </div>
        );
    }

    authenticate() {
        fetch('/api/login',
        {
            method: 'GET',
            headers: {
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
        .then(response => {
            if (response.status === 200) {
            } else {
                throw Error(response.statusText);
            }

            return response.json();
        })
        .then(items => {
            this.props.onAuthenticate({
                     userName: items.userName,
                     authenticated: items.authenticated
                 });
        })
        .catch((error) => {
            browserHistory.push('/login');
        });
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
