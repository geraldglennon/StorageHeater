import React, { Component } from 'react';
import MainAppMenu from './components/MenuBar/MainAppMenu';
import DisplayTable from './components/DisplayTable/DisplayTable';
import Details from './components/Storage/view/View';
import Welcome from './components/Welcome/Welcome';
import Login from './components/Login/Login';

import './App.css';

import { Router, Route, browserHistory } from 'react-router'

class App extends Component {

    constructor () {
        super();
        this.state = {
           isAuthenticated: false
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
                    {this.state && this.state.isAuthenticated &&
                        <div className="app-menu">
                            <MainAppMenu />
                        </div>
                    }
                    <div className="app-content ">
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
                this.setState({isAuthenticated: true});
            } else {
                throw Error(response.statusText);
            }
        })
        .catch((error) => {
            browserHistory.push('/login');
        });
    }
}

export default App;
