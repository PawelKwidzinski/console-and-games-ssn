import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReturnGamesComponent } from './return-games.component';

describe('ReturnGamesComponent', () => {
  let component: ReturnGamesComponent;
  let fixture: ComponentFixture<ReturnGamesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReturnGamesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReturnGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
