import React, { Component } from 'react';


import Editor from '../../ReactJSONEditor';
import './View.css';
import {RIEInput, RIETextArea} from 'riek';
import {browserHistory} from 'react-router';
import {ModalContainer, ModalDialog} from 'react-modal-dialog';
import SelectBox from 'react-select';
import 'react-select/dist/react-select.css';
import ReactNotify from 'react-notify';
import '../../../Notifications.css';

export default class DetailedView extends Component {

 constructor() {
        super();

        this.defaultValue = {
            id: null,
            data: {properties: {}},
            name: 'Enter name here',
            description: 'Enter description here',
            label: 'No Label',
            lockedText: 'Lock',
            unlockedText: 'Unlock',
            modeButtonText: 'Edit',
            mode: 'view',
            isShowingDeleteModal: false,
            versions: [],
            currentVersion: ""
        };

        this.state = this.defaultValue;
    }

    save() {
        let method = 'POST';
        let url = `/api/storage/`;

        if (this.state.id) {
            method = 'PUT'
            url += `${this.state.currentVersionId}/version`
        }

        fetch(url,
        {
            method: method,
            headers: {
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.state.data)
        })
          .then(response => {
            if (this.validateHttpRequest(response.status)) {
                throw Error(response.statusText);
            }
            return response.json();
          })
          .then(items => {
            this.setStateForCreateOrSave(items);
          })
          .then(() => {
            this.refs.notificator.success("Save", "Saved successfully", 5000);
          })
          .catch((error) => {
              this.refs.notificator.error("Save Failed", "Save was unsuccessfully", 5000);
          });
    }

    createNewVersion () {
        fetch(`/api/storage/${this.state.id}/version`,
        {
            method: 'POST',
            headers: {
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'
            }
        })
          .then(response => {
            if (this.validateHttpRequest(response.status)) {
                throw Error(response.statusText);
            }
            return response.json();
          })
          .then(items => {
            this.setState({
                 id: items.id,
                 name: items.name,
                 description: items.description,
                 versions: items.versions,
            });

            if (items.versions.length > 0) {
                this.fetchVersion(items.id, items.versions[0]);
            }
          })
          .then(() => {
            this.refs.notificator.success("New Version Created", "A new version has been created successfully", 5000);
          })
          .catch((error) => {
              this.refs.notificator.error("Failed", "A new version was not created", 5000);
          });
    }

    setStateForCreateOrSave (items) {
        if (this.state.id) {
              this.setState({
                 data: items,
                 locked: items.locked,
                 currentVersionId: items.id,
                 currentVersion: this.formatVersion(items.version)
              });

              this.updateParent();
        } else {
             this.setState({
                 id: items.id,
                 name: items.name,
                 description: items.description,
                 versions: items.versions
             });

             if (items.versions.length > 0) {
                 this.fetchVersion(items.id, items.versions[0]);
             }
        }
    }

    deleteData() {
        fetch(`/api/storage/${this.state.id}`, {
                    method: 'DELETE'
                })
                .then(response => {
                  if (this.validateHttpRequest(response.status)) {
                      throw Error(response.statusText);
                  }
                })
                .then(() => browserHistory.push('/storage'))
                .catch((error) => {
                    this.refs.notificator.error("Delete Failed", "Failed to delete", 5000);
                });

        this.setState({isShowingDeleteModal: false});
    }

    fetch(id) {
        fetch(`/api/storage/${id}`)
            .then(response => {
                if (this.validateHttpRequest(response.status)) {
                    throw Error(response.statusText);
                }
                return response.json();
            })
            .then(items => {
                this.setState({
                    id: items.id,
                    name: items.name,
                    description: items.description,
                    versions: items.versions
                });

                if (items.versions.length > 0) {
                    this.fetchVersion(items.id, items.versions[0]);
                }
            })
            .catch((error) => {
                this.refs.notificator.error("Failed to Retrieve Data", "Failed to get data from server", 5000);
            });
    }

    fetchVersion(id, version) {
        fetch(`/api/storage/${id}/version/${version}/`)
            .then(response => {
                if (this.validateHttpRequest(response.status)) {
                    throw Error(response.statusText);
                }
                return response.json();
            })
            .then(items=> {
                this.setState({
                    data: items,
                    locked: items.locked,
                    currentVersionId: items.id,
                    currentVersion: version,
                    label: items.label != null ? items.label : this.defaultValue.label
                });
            })
            .catch((error) => {
                this.refs.notificator.error("Failed to Retrieve Data", "Failed to get version from server", 5000);
        });
    }

    updateParent() {
        let data = {
            id: this.state.id,
            name: this.state.name,
            description: this.state.description,
            versions: this.state.versions
        }

        fetch(`/api/storage/`, {
                method: 'PUT',
                headers: {
                          'Accept': 'application/json',
                          'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (this.validateHttpRequest(response.status)) {
                    throw Error(response.statusText);
                }
                return response.json();
            })
            .then(items=> {
                 this.setState({
                    id: items.id,
                    name: items.name,
                    description: items.description,
                    versions: items.versions
                 });
            })
            .catch((error) => {
                this.refs.notificator.error("Failed to update", "Failed to update storage meta data", 5000);
        });
    }

    lockOrUnlock(lock) {
        fetch(`/api/storage/lock/${this.state.currentVersionId}/${lock}`,
        {
            method: 'PUT',
            headers: {
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'
            }
        });
    }

    handleChange(data) {
        this.state.data.properties = data;
    }

    componentWillMount() {
      if (this.props.location.query.id) {
        const _id = this.props.location.query.id;
        this.setState({id: _id});
        this.fetch(_id);
      } else {
        this.setState(this.defaultValue);
      }
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.location.query.id) {
            const _id = nextProps.location.query.id;
            this.setState({id: _id});
            this.fetch(_id);
        } else {
            this.setState(this.defaultValue);
        }

    }

    handleNameChange(props) {
        this.state.data.name = props.title;
        this.setState({name: props.title});
    }

    handleDescriptionChange(props) {
         this.setState({description: props.description});
         this.state.data.description = props.description;
    }

    handleLockChange() {
         if (this.state.locked) {
            this.setState({locked: false});
            this.state.data.locked = false;
            this.lockOrUnlock(false);
         } else {
            this.setState({locked: true});
            this.state.data.locked = true;
            this.lockOrUnlock(true);
         }
    }

    handleModeChange() {
        if (this.state.mode === 'view') {
            this.setState({mode: 'code'});
            this.setState({modeButtonText: 'View'});
        } else {
            this.setState({mode: 'view'});
            this.setState({modeButtonText: 'Edit'});
        }
    }

    handleVersionChange(props) {
        this.fetchVersion(this.state.id, props.value);
    }

    handleNewVersionChange() {
        this.createNewVersion();
    }

    handleLabelChange(props) {
         this.setState({label: props.title});

         let data = {
            name: props.title,
            parentId: this.state.id,
            version: this.state.currentVersion
         };

         fetch(`/api/storage/labels`,
         {
             method: 'PUT',
             headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
             },
             body: JSON.stringify(data)
         })
           .then(response => {
             if (this.validateHttpRequest(response.status)) {
                 throw Error(response.statusText);
             }
           })
           .then(() => {
             this.refs.notificator.success("New Label Added", "A label has been added successfully", 5000);
           })
           .catch((error) => {
               this.refs.notificator.error("Failed", "A label could not be added", 5000);
           });
    }

    openDeleteDialog = () => this.setState({isShowingDeleteModal: true})
    closeDeleteDialog = () => this.setState({isShowingDeleteModal: false})

    validate(props) {
        if (props.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    validateHttpRequest(code) {
        if (code >= 300) {
            return true;
        } else {
            return false;
        }
    }

    convertToSelectObj(obj) {
        if (!obj) {
            return [];
        }
        return obj.map(function (item) {
            return {value: item, label: 'ver. ' + item};
        });
    }

    formatVersion (versionObj) {
        return versionObj.major + '.' + versionObj.minor + '.' + versionObj.build;
    }

    render() {

        let lockedButtonText = this.state.lockedText;

        let options = this.convertToSelectObj(this.state.versions);

        if (this.state.locked) {
            lockedButtonText = this.state.unlockedText;
        }
		
		let newVersionButton, lockButton, deleteButton = false;
		if (this.state.id) {
			newVersionButton = <button className='btn detailedView-newVersionButton' onClick={this.handleNewVersionChange.bind(this)}>New Version</button>;
			lockButton = <button className='btn detailedView-lockButton' onClick={this.handleLockChange.bind(this)}>{lockedButtonText}</button>;
			deleteButton = <button className='btn detailedView-deleteButton' onClick={this.openDeleteDialog.bind(this)}>Delete</button>;
		}
			

        return (
            <div className='detailedView'>
                <h3>Detailed View</h3>
                <hr></hr>

                <div className='detailedView-versionSelect'>


                    <SelectBox name='Version'
                               className='detailedView-version-select'
                               onChange={this.handleVersionChange.bind(this)}
                               options={options}
                               value={this.state.currentVersion}
                               clearable={false} />
                </div>

                <h4>Name</h4>
                <RIEInput
                  value={this.state.name}
                  change={this.handleNameChange.bind(this)}
                  propName='title'
                  validate={this.validate.bind(this)}
                  isDisabled={false} />

                <h4>Label</h4>
                <div className='detailedView-label'>
                    <RIEInput
                      value={this.state.label}
                      change={this.handleLabelChange.bind(this)}
                      propName='title'
                      validate={this.validate.bind(this)}
                      isDisabled={false} />
                </div>

                <h4>Description</h4>
                <div className='detailedView-description'>
                    <RIETextArea
                      value={this.state.description}
                      change={this.handleDescriptionChange.bind(this)}
                      propName='description'
                      validate={this.validate.bind(this)}
                      isDisabled={false} />
                </div>

                <div className='detailedView-panel'>
                    <div className='detailedView-buttons'>
                        <button className='btn detailedView-modeButton' onClick={this.handleModeChange.bind(this)}>{this.state.modeButtonText}</button>
						{newVersionButton}
						{lockButton}
                        <button className='btn detailedView-saveButton' onClick={this.save.bind(this)}>Save</button>
						{deleteButton}
                    </div>
                </div>
                <div className='detailedView-body'>
                    <Editor json={this.state.data.properties}
                        height={300} onChange={this.handleChange.bind(this)}
                        mode={this.state.mode}
                    />
                </div>
                {
                    this.state.isShowingDeleteModal &&
                    <ModalContainer onClose={this.closeDeleteDialog}>
                      <ModalDialog onClose={this.closeDeleteDialog}>
                        <h1>Delete</h1>
                        <p>Are you sure you want to delete?</p>
                        <div className='detailedView-deleteDialog-panel'>
                            <button className='detailedView-deleteDialog-okButton' onClick={this.deleteData.bind(this)}>OK</button>
                            <button className='detailedView-deleteDialog-cancelButton' onClick={this.closeDeleteDialog.bind(this)}>Cancel</button>
                        </div>
                      </ModalDialog>
                    </ModalContainer>
                }
                <ReactNotify ref='notificator'/>
            </div>
        );
    }
}

