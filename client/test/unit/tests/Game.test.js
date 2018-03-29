import { shallow } from 'vue-test-utils'
import sinon from 'sinon'
import game from '@/components/Game'

jest.mock('@/components/GameMenu', () => {})

describe('Game.vue', () => {
  /* ================================================================
    Test will check if component  function attempt login is called
    ==================================================================
  */
  test('Calls toggleSidebar() function for side bar options', () => {
  // Inistilize test
    const listener = sinon.spy()
    const wrapper = shallow(game)
    wrapper.setMethods({
      toggleSidebar: listener // listens out for this method
    })
    // click the button
    wrapper.find('.menu-icon').trigger('click')
    // Assert result expect the method to be call was it called when the button was called
    expect(listener.called).toBe(true)
  })
})
