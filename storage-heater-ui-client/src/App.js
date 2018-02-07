import React, { Component } from 'react';
import MainAppMenu from './components/MenuBar/MainAppMenu';
import DisplayTable from './components/DisplayTable/DisplayTable';
import Details from './components/Storage/view/View';
import Welcome from './components/Welcome/Welcome';

import './App.css';

import { Router, Route, browserHistory } from 'react-router'

class App extends Component {

    componentDidMount() {
        document.title = "Storage Heater"
    }

    render () {
        return (
            <div className="App">
                <div className="App-header">    
                    <h2>Storage Heater</h2>
                </div>
                <div className="app-body">
                    <div className="app-menu">
                        <MainAppMenu />
                    </div>
                    <div className="app-content ">
                    <Router history={browserHistory}>
                        <Route path="/" component={Welcome} />
                        <Route path="/storage" component={DisplayTable} />
                        <Route path="/view" component={Details} />
                      </Router>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
