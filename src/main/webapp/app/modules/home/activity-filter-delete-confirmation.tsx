import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export interface IActivityFilterDeleteConfirmationProps {
  selectedFilter: any;
  handleClose: any;
  handleConfirmDelete: any;
}

export class ActivityFilterDeleteConfirmation extends React.Component<IActivityFilterDeleteConfirmationProps> {
  confirmDelete = event => {
    this.props.handleConfirmDelete(this.props.selectedFilter.id);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.handleClose();
  };

  render() {
    const { selectedFilter } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody id="serviceNetApp.language.delete.question">
          <Translate contentKey="serviceNetApp.activity.home.filter.deleteQuestion" interpolate={{ id: selectedFilter.id }}>
            Are you sure you want to delete this Activity Filter?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button id="jhi-confirm-delete-language" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

export default ActivityFilterDeleteConfirmation;
