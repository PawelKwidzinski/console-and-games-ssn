import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BorrowedGameListComponent } from './borrowed-game-list.component';

describe('BorrowedGameListComponent', () => {
  let component: BorrowedGameListComponent;
  let fixture: ComponentFixture<BorrowedGameListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BorrowedGameListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BorrowedGameListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
