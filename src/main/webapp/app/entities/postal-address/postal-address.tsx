import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import {
  Translate,
  translate,
  ICrudGetAllAction,
  TextFormat,
  JhiPagination,
  getPaginationItemsNumber,
  getSortState,
  IPaginationBaseState
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import PageSizeSelector from '../page-size-selector';
import { IRootState } from 'app/shared/reducers';
import { getEntities, updateEntity } from './postal-address.reducer';
import { ITEMS_PER_PAGE_ENTITY, MAX_BUTTONS } from 'app/shared/util/pagination.constants';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostalAddressProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IPostalAddressState extends IPaginationBaseState {
  dropdownOpenTop: boolean;
  dropdownOpenBottom: boolean;
  itemsPerPage: number;
}

export class PostalAddress extends React.Component<IPostalAddressProps, IPostalAddressState> {
  constructor(props) {
    super(props);

    this.toggleTop = this.toggleTop.bind(this);
    this.toggleBottom = this.toggleBottom.bind(this);
    this.select = this.select.bind(this);
    this.state = {
      dropdownOpenTop: false,
      dropdownOpenBottom: false,
      itemsPerPage: ITEMS_PER_PAGE_ENTITY,
      ...getSortState(this.props.location, ITEMS_PER_PAGE_ENTITY)
    };
  }

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    const { activePage, sort, order } = this.state;
    this.props.history.push(`${this.props.location.pathname}?page=${activePage}&sort=${sort},${order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.updatePage());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  toggleTop() {
    this.setState({ dropdownOpenTop: !this.state.dropdownOpenTop });
  }

  toggleBottom() {
    this.setState({ dropdownOpenBottom: !this.state.dropdownOpenBottom });
  }

  select = prop => () => {
    this.setState(
      {
        itemsPerPage: prop
      },
      () => this.updatePage()
    );
  };

  updatePage() {
    window.scrollTo(0, 0);
    this.sortEntities();
  }

  render() {
    const { postalAddressList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="postal-address-heading">
          <Translate contentKey="serviceNetApp.postalAddress.home.title">Postal Addresses</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.postalAddress.home.createLabel">Create new Postal Address</Translate>
          </Link>
        </h2>
        <Row className="justify-content-center">
          <PageSizeSelector
            dropdownOpen={this.state.dropdownOpenTop}
            toggleSelect={this.toggleTop}
            itemsPerPage={this.state.itemsPerPage}
            selectFunc={this.select}
          />
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={MAX_BUTTONS}
          />
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.attention">Attention</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.address1">Address 1</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.address2">Address 2</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.region">Region</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.stateProvince">State Province</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.postalCode">Postal Code</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.country">Country</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.postalAddress.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {postalAddressList.map((postalAddress, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${postalAddress.id}`} color="link" size="sm">
                      {postalAddress.id}
                    </Button>
                  </td>
                  <td>{postalAddress.attention}</td>
                  <td>{postalAddress.address1}</td>
                  <td>{postalAddress.address2}</td>
                  <td>{postalAddress.city}</td>
                  <td>{postalAddress.region}</td>
                  <td>{postalAddress.stateProvince}</td>
                  <td>{postalAddress.postalCode}</td>
                  <td>{postalAddress.country}</td>
                  <td>
                    {postalAddress.locationName ? (
                      <Link to={`location/${postalAddress.locationId}`}>{postalAddress.locationName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${postalAddress.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${postalAddress.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${postalAddress.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <PageSizeSelector
            dropdownOpen={this.state.dropdownOpenBottom}
            toggleSelect={this.toggleBottom}
            itemsPerPage={this.state.itemsPerPage}
            selectFunc={this.select}
          />
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={MAX_BUTTONS}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ postalAddress }: IRootState) => ({
  postalAddressList: postalAddress.entities,
  totalItems: postalAddress.totalItems
});

const mapDispatchToProps = {
  getEntities,
  updateEntity
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostalAddress);
