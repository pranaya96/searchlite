import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdButtonModule, MdInputModule, MdListModule, MdToolbarModule, MdIconModule, MdSnackBarModule } from '@angular/material';
import {MdIconRegistry} from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SearchService } from './search.service';
import { AppComponent } from './app.component';
import {DomSanitizer} from '@angular/platform-browser';
import {FlexLayoutModule} from '@angular/flex-layout';
import { RouterModule, Routes} from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ResultsComponent } from './results/results.component';
import { MdCardModule } from '@angular/material';
import { MaterialModule } from '@angular/material';

const appRoutes: Routes = [

  {path: '', component: HomeComponent},
  {path: 'results/:term', component: ResultsComponent}
  
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
    MdCardModule,
    MaterialModule,
    FlexLayoutModule,
    MdSnackBarModule,

  ],
  providers: [SearchService,],
  bootstrap: [AppComponent]
})
export class AppModule { }
export class IconSvgExample { }
