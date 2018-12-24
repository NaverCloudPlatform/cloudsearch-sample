import * as React from 'react';
import { mount } from 'enzyme';
import * as ReactDOM from 'react-dom';
import { App } from './App';

function setup() {
  const props = {
  };
  const wrapper = mount(<App {...props} />);
  return {
    props,
    wrapper
  }
}

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
  ReactDOM.unmountComponentAtNode(div);
});

it('should be call change handler on change search text', () => {
  const { wrapper } = setup();
  wrapper.setState({
    keyword: ''
  });
  wrapper.find('input').simulate('change', {
    target: {value: 'test'}
  });
  expect(wrapper.state('keyword')).toEqual('test');
});
