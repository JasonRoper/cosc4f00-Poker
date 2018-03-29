
import { getEnum, Test } from '@/api/testings'

jest.mock('@/api/pokerclient', () => ({
  send (path, payload) {
    console.log('sending to path', path, payload)
  }
}))

test('getEnum works', () => {
  expect(getEnum(1)).toBe(Test.ONE)
  expect(getEnum(2)).toBe(Test.TWO)
  expect(getEnum(3)).toBe(Test.THREE)
  expect(getEnum(-1234)).toBe(Test.NONE)
})

import GameMenu from '@/components/GameMenu'
import { shallow } from 'vue-test-utils'

// THIS IS MOCKED
import GameRequest from '@/store/GameRequest'

jest.mock('@/store/GameRequest.ts', () => ({
  fail: false,
  constructor () {
  },
  createCompetitiveGame () {
    if (!this.fail) {
      return Promise.resolve()
    } else {
      return Promise.reject()
    }
  }
}))

describe('GameMenu should work', () => {
  test('clicking competitive game should try to create a new matchmaking request', () => {
    // not currently working
    const mount = shallow(GameMenu, {router: {
      push (payload) {
        console.log(payload, 'was pushed onto the router')
      }
    }})
    GameRequest.fail = false

    // mount.find('competitiveGame').click()
    // what happens when competitive game succeeds
  })
  test('when createCompetetiveGame fails', () => {
    const mount = shallow(GameMenu)
    GameRequest.fail = true

    // mount.find('competitiveGame').click()
    // what happens when competitive game fails
  })
})
