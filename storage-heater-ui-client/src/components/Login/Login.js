import React, { Component } from 'react';
import { browserHistory } from 'react-router';
import './Login.css'
import { connect } from 'react-redux';
import { getUser } from '../actions/actions';

const mapStateToProps = state => ({
  user: state.user
});

const mapDispatchToProps = dispatch => ({
  onLogin: (user) => {
    dispatch(getUser(user));
  }
});

class Login extends Component {
    constructor () {
        super();
        this.state = {
            username: '',
            password: '',
            error: '',
            isAuthenticated: false,
            user: {userName: '', authenticated: false}
        };
    }

    validateForm() {
        return this.state.username.length === 0 && this.state.password.length === 0;
    }

    handleUsername(e) {
        this.setState({username: e.target.value});
    }

    handlePassword(e) {
        this.setState({password: e.target.value});
    }

    handleSubmit = e => {
        e.preventDefault();

        if (this.validateForm()) {
            this.setState({error: 'Username or password field is empty'});
            return;
        }

        let userCredentials = {
            username: this.state.username,
            password: this.state.password
        }
        fetch('/api/login',
        {
            method: 'POST',
            headers: {
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'
            },
            credentials: 'same-origin',
            body: JSON.stringify(userCredentials)
        })
        .then(response => {
            if (response.status === 200) {
                this.setState({isAuthenticated: true, error: ''});
                this.props.onLogin({
                    userName: this.state.username,
                    authenticated: true
                    });
                browserHistory.push('/');
            } else {
                throw Error(response.statusText);
            }
          return response;
        })
        .catch((error) => {
            this.setState({
                username: '',
                password: ''
            });
            this.setState({error: 'User credentials are invalid or incorrect'});
        });
    }

    render () {
        return (
           <div className="Login">
               <form onSubmit={this.handleSubmit.bind(this)}>
                 <h3>Login</h3>
                 <input type="text" placeholder="Username" value={this.state.username} onChange={this.handleUsername.bind(this)}/>
                 <input type="password" placeholder="Password" value={this.state.password} onChange={this.handlePassword.bind(this)}/>
                 <input type="submit" value="Login"/>
                  { this.state && this.state.error &&
                    <div className="error">{this.state.error}</div>
                  }
               </form>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login);
