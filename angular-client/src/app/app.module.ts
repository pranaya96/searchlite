import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdButtonModule, MdInputModule, MdListModule, MdToolbarModule, MdIconModule } from '@angular/material';
import {MdIconRegistry} from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SearchService } from './search.service';
import { AppComponent } from './app.component';
import {DomSanitizer} from '@angular/platform-browser';
import { ResultsComponent } from './results/results.component';
import { RouterModule, Routes} from '@angular/router';
import { HomeComponent } from './home/home.component';


const appRoutes: Routes = [

  {path: 'home', component: HomeComponent},
  {path: 'results', component: ResultsComponent}
  
]

@NgModule({
  declarations: [
    AppComponent,
    ResultsComponent,
    HomeComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    MdInputModule,
    HttpModule,
    MdListModule,
    MdToolbarModule,
    MdButtonModule,
    MdIconModule,
  ],
  providers: [SearchService],
  bootstrap: [AppComponent]
})
export class AppModule { }
export class IconSvgExample { }
