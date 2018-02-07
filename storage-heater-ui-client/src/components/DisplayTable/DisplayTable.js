import React, { Component } from 'react';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import ReactNotify from 'react-notify';
import '../../Notifications.css';

class DisplayTable extends Component {
    constructor () {
        super();
        this.state = {
            items: [],
            pages: 0,
            page: 1,

            pageSize: 20,
            loading: false,
            searchInput: ''
        };
    }

    componentDidMount () {
        this.getData();
    }

    getData (pageIndex, pageSize, query) {
        this.setState({loading: true});
        if (pageSize) {
            this.setState({pageSize: pageSize});
        } else {
            pageSize = this.state.pageSize;
        }

        if (!pageIndex) {
            pageIndex = 0;
        }

        let url = `/api/storage?page=${pageIndex}&perPage=${pageSize}`;

        if (query && query.length > 0) {
            url += query;
        } else if (this.state.searchInput.length > 0) {
            url += '&q=' + encodeURIComponent(`name~=${this.state.searchInput}`);
        }

        fetch(url)
            .then(response => {
                if (this.validateHttpRequest(response.status)) {
                    throw Error(response.statusText);
                }
                return response.json();
            })
            .then(items=> {
                const pages = Math.ceil(items.totalElements / this.state.pageSize);
                this.setState({
                    items: items.content,
                    pages: pages,
                    page: pageIndex,
                    loading: false
                });
            })
            .catch((error) => {
                this.refs.notificator.error("Failed to Retrieve Data", "Failed to get data from server", 5000);
            });
    }

    validateHttpRequest(code) {
        if (code >= 300) {
            return true;
        } else {
            return false;
        }
    }

    handleSearchChange(e) {
        this.state.searchInput =  e.target.value;
        if (e.target.value.length === 0) {
            this.getData();
        }
    }

    search() {
        let query = '&q=' + encodeURIComponent(`name~=${this.state.searchInput}`);
        this.getData(null, null, query);
    }

    render () {
        const columns = [{
            header: 'Name',
            accessor: 'name',
            sortable: true,
            render: props=>
                <a target='_self' href={'/view?id=' + props.row.id}>{props.value}</a>
        }, {
            header: 'Description',
            accessor: 'description',
            sortable: true
        }, {
            header: 'No. Of Versions',
            accessor: 'versions',
            sortable: true,
            render: props => <span className='versions'>{'' + props.row.versions.length}</span>
        }];

        return (
                <div className="app-table">
                    <h3>Data Storage Table</h3>
                    <div className='btn app-table-search-panel'>
                        <input type="text" className="app-table-searchInput" onChange={this.handleSearchChange.bind(this)}/>
                        <button className='app-table-searchButton' onClick={this.search.bind(this)}>Search</button>
                    </div>
                    <ReactTable className={'-striped'}
                                data={this.state.items}
                                pages={this.state.pages}
                                page={this.state.page}
                                manual loading={this.state.loading}
                                columns={columns}
                                pageSize={this.state.pageSize}
                                onPageChange={(pageIndex) => {this.getData(pageIndex)}}
                                onPageSizeChange={(pageSize, pageIndex) => {this.getData(pageIndex, pageSize)}}/>
                    <ReactNotify ref='notificator'/>
                </div>
        );
    }
}

export default DisplayTable;
