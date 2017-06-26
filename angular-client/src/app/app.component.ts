import { Component } from '@angular/core';
import { SearchService } from './search.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  term: string;
  results: string[];
  title = 'Got qWest?'
  constructor(private searchService: SearchService) {}

  search() {
    if (this.term === 'dog')
    {
      this.results = ['file1.pdf'];
    }
    if (this.term == 'Nepal')
    {
      this.results = ['Nepal is a great place'];
    }
    if (this.term == 'America')
    {
      this.results = ['Some link to America'];
      }
    
  }
}
