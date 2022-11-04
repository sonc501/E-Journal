import styled from 'styled-components'

const Wrapper = styled.article`
  background: var(--white);
  border-radius: var(--borderRadius);
  display: grid;
  grid-template-rows: 1fr auto;
  box-shadow: var(--shadow-2);
  -
  header {
    padding: 1rem 1.5rem;
    border-bottom: 1px solid var(--grey-100);
    display: grid;
    grid-template-columns: 1fr;
    align-items: center;
    h5 {
      letter-spacing: 0;
    }
  }
  .info {
    h5 {
      margin-bottom: 0.25rem;
    }
    
    p {
      margin: 0;
      text-transform: capitalize;
      color: var(--grey-400);
      letter-spacing: var(--letterSpacing);
   
    }
  }
  .pending {
    background: #fcefc7;
    color: #e9b949;
  }
  .accepted {
    background: #93c47d;
    color: #647acb;
  }
  .reviewing {
    background: #93c47d;
    color: #647acb;
  }
  .rejected {
    color: #d66a6a;
    background: #ffeeee;
  }
  .content {
    padding: 1rem 1.5rem;
  }
  .content-center {
    witdh: 100%;
    .p {
      display:inline-block;
    }
  }

  .status {
    border-radius: var(--borderRadius);
    text-transform: capitalize;
    letter-spacing: var(--letterSpacing);
    text-align: left;
    width: 100px;
    height: 30px;
  }
  footer {
    margin-top: 1rem;
  }
  .edit-btn,
  .delete-btn {
    letter-spacing: var(--letterSpacing);
    cursor: pointer;
    height: 30px;
  }
  .edit-btn {
    color: var(--green-dark);
    background: var(--green-light);
    margin-right: 0.5rem;
  }
  .delete-btn {
    color: var(--red-dark);
    background: var(--red-light);
  }
.actions > * {
  margin: 5px;
}
  &:hover .actions {
    visibility: visible;
  }
`

export default Wrapper
