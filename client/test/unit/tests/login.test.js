import { shallow } from 'vue-test-utils'
// import sinon from 'sinon'
import login from '@/components/login.vue'

describe('login.vue', () => {
  /*
  * Test will check if the name of the component is login
  */
  it('Is called login', () => {
    // Initialize the test .
    const wrapper = shallow(login)
    // Assert result
    expect(wrapper.name().to.equal('login'))
  })
})
