import React, { Component } from 'react';
import './Welcome.css';
import storageList from '../../../resources/database.png';
import create from '../../../resources/add.png';
import { browserHistory } from 'react-router';

class Welcome extends Component {
    constructor () {
        super();
        this.state = {

        };
    }

    handleCreate() {
         browserHistory.push('/view');
    }

    handleList() {
         browserHistory.push('/storage');
    }

    render () {
        return (
            <div>
                <p className="App-intro">
                    This is an application for storing information in json format. It will allow the user to create, edit, search and delete json data.
                </p>
                <div className="options_block">
                    <div className="storage_list box">
                        <h3>Storage List</h3>
                        <img src={storageList} alt="Storage List" onClick={this.handleList} />
                    </div>
                    <div className="create_storage box">
                        <h3>Create</h3>
                        <img src={create} alt="Create" onClick={this.handleCreate}/>
                    </div>
                </div>
            </div>
        );
    }
}

export default Welcome;
