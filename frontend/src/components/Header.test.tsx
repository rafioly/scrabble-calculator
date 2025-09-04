import { render, screen } from '@testing-library/react';
import Header from './Header';

test('renders the main header', () => {
  // Arrange
  render(<Header />);

  // Act
  const headerElement = screen.getByText(/Scrabble Points Calculator/i);

  // Assert
  expect(headerElement).toBeInTheDocument();
});
