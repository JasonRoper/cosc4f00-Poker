import PokerClient from './pokerclient'

export enum Test {
    ONE,
    TWO,
    THREE,
    NONE
}
export class T {

  constructor (public name: string) {
  }

  public getName () {
    return name + 'asdf'
  }
}

export function getEnum (num: number): Test {
  PokerClient.send('/testing/test', { things: 'HELLO!!!', num })

  switch (num) {
    case 1:
      return Test.ONE
    case 2:
      return Test.TWO
    case 3:
      return Test.THREE
    default:
      return Test.NONE
  }

}
