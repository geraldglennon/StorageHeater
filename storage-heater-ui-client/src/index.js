import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';
import { Provider } from 'react-redux'
import { createStore } from 'redux'

const user = (state = {}, action) => {
  switch (action.type) {
    case 'GET_USER':
        let newState = Object.assign({}, state)
        newState.user = action.payload;
        return newState;
    default:
      return state;
  }
};

const store = createStore(user);

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root')
);
