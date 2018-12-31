import * as React from 'react';
import { default as axios } from 'axios';
import withStyles from '@material-ui/core/styles/withStyles';
import {
  AppBar,
  Grid,
  IconButton,
  Paper,
  Table, TableBody, TableCell,
  TableHead, TableRow,
  TextField,
  Toolbar,
  Tooltip,
  Typography
} from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';
import RefreshIcon from '@material-ui/icons/Refresh';

const styles = (theme: any) => ({
  paper: {
    maxWidth: 936,
    margin: 'auto',
    overflow: 'hidden',
  },
  searchBar: {
    borderBottom: '1px solid rgba(0, 0, 0, 0.12)',
  },
  searchInput: {
    fontSize: theme.typography.fontSize,
  },
  block: {
    display: 'block',
  },
  addUser: {
    marginRight: theme.spacing.unit,
  },
  contentWrapper: {
    margin: '40px 16px',
  },
});

interface Props {
  classes?: any;
}
interface States {
  keyword: string;
  result: any[];
}

export class AppComp extends React.Component<Props, States> {
  constructor(props: Props) {
    super(props);
    this.state = {
      keyword: '',
      result: []
    };
    this.handleSearch = this.handleSearch.bind(this);
    this.handleRefresh = this.handleRefresh.bind(this);
  }

  handleQueryKeyPress = (e: any) => {
    if (e.key === 'Enter') {
      this.handleSearch();
    }
  }

  handleChange = (e: any) => {
    this.setState({
      keyword: e.target.value
    });
  }

  handleSearch() {
    const searchQuery = {
      search: {
        name_key: {
          main: {query: this.state.keyword}
        }
      }
    };
    axios.post('/api/domain/free-pdf-books/document/search', searchQuery)
      .then((response: any) => {
        if (response.data.result && response.data.result.items) {
          this.setState({
            result: response.data.result.items
          }, () => console.log(this.state.result));
        }
      })
      .catch((error: any) => {
        this.setState({
          result: []
        });
        console.log(error);
      })
      .then(() => {
        // todo
      });
  }

  handleRefresh() {
    this.setState({
      keyword: '',
      result: []
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <Paper className={classes.paper}>
        <AppBar className={classes.searchBar} position="static" color="default" elevation={0}>
          <Toolbar>
            <Grid container={true} spacing={16} alignItems="center">
              <Grid item>
                <SearchIcon className={classes.block} color="inherit" onClick={this.handleSearch} />
              </Grid>
              <Grid item xs>
                <TextField
                  fullWidth
                  placeholder="Search by name"
                  InputProps={{
                    disableUnderline: true,
                    className: classes.searchInput,
                    onChange: this.handleChange,
                    onKeyDown: this.handleQueryKeyPress,
                    value: this.state.keyword
                  }}
                />
              </Grid>
              <Grid item>
                <Tooltip title="Refresh">
                  <IconButton>
                    <RefreshIcon className={classes.block} color="inherit" onClick={this.handleRefresh} />
                  </IconButton>
                </Tooltip>
              </Grid>
            </Grid>
          </Toolbar>
        </AppBar>
        <div className={classes.contentWrapper}>
          {
            this.state.result.length === 0 && (<Typography color="textSecondary" align="center">
              No result
            </Typography>)
          }
          {
            this.state.result.length > 0 && (<Table className={classes.table}>
              <TableHead>
                <TableRow>
                  <TableCell>Topic</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Link</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {
                  this.state.result.map(row => (
                    <TableRow key={row.name}>
                      <TableCell>{row.topic}</TableCell>
                      <TableCell>{row.name}</TableCell>
                      <TableCell>
                        <a href={`https://github.com/iMarcoGovea/books/blob/master/${row.url}`} target="_blank">
                          {row.name}
                        </a>
                      </TableCell>
                    </TableRow>
                  ))
                }
              </TableBody>
            </Table>)
          }
        </div>
      </Paper>
    );
  }
}

export const App = withStyles(styles)(AppComp);
